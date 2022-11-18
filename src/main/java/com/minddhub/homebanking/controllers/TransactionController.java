package com.minddhub.homebanking.controllers;

import com.minddhub.homebanking.dtos.ClientDTO;
import com.minddhub.homebanking.dtos.TransactionDTO;
import com.minddhub.homebanking.models.Account;
import com.minddhub.homebanking.models.Client;
import com.minddhub.homebanking.models.Transaction;
import com.minddhub.homebanking.models.TransactionType;
import com.minddhub.homebanking.repositories.AccountRepository;
import com.minddhub.homebanking.repositories.ClientRepository;
import com.minddhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
//Defino la ruta base
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    //GET de todas las Transactions
    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactions() {
        //stream() procesa y transforma cada elemento de la lista que retorna findAll()
        //y map() ejecuta el método constructor que está en AccountDTO
        return transactionRepository.findAll().stream().map(TransactionDTO::new).collect(toList());
    }

    //GET de una Transaction por ID
    @GetMapping("/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return transactionRepository.findById(id).map(TransactionDTO::new).orElse(null);
    }

    //GET de las Transactions por type
    @GetMapping("/transactions/type")
    public List<TransactionDTO> getTransactionsType(@RequestParam TransactionType type) {
        return transactionRepository.findByType(type).stream().map(TransactionDTO::new).collect(toList());
    }

    //GET de las Transactions entre dos fechas
    @GetMapping("/transactions/betweenDates")
    public List<TransactionDTO> getTransactionsBetweenDates(@RequestParam String date1, @RequestParam String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(date1.isEmpty() || date2.isEmpty()) {
            return null;
        }
        return transactionRepository.findByDateBetween(LocalDateTime.parse(date1, formatter), LocalDateTime.parse(date2, formatter)).stream().map(TransactionDTO::new).collect(toList());
    }

    //GET de las Transactions entre dos amounts
    @GetMapping("/transactions/amountsBetween")
    public List<TransactionDTO> findByAmountGreaterThanAndLessThan(@RequestParam double amount1, @RequestParam double amount2) {
        return transactionRepository.findByAmountBetween(amount1, amount2).stream().map(TransactionDTO::new).collect(toList());
    }

    //POST para crear una transacción entre Accounts
    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createClient(Authentication authentication,
                                               @RequestParam double amount,
                                               @RequestParam String description,
                                               @RequestParam String fromAccountNumber,
                                               @RequestParam String toAccountNumber) {
        Client client = this.clientRepository.findByEmail(authentication.getName()).get();
        new ClientDTO(client);
        //Si falta algún parámetro devuelvo mensaje de error
        if (amount <= 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        Optional<Account> account1 = accountRepository.findByNumber(fromAccountNumber);
        Optional<Account> account2 = accountRepository.findByNumber(toAccountNumber);
        //Valido que las Account de origen y destino no sean iguales
        if(fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Forbidden petition. You can´t make a transaction to the same account", HttpStatus.FORBIDDEN);
        }
        //Valido que exista la Account de origen
        if(!accountRepository.findByNumber(fromAccountNumber).isPresent()) {
            return new ResponseEntity<>("Forbidden petition. The origin account doesn´t exist", HttpStatus.FORBIDDEN);
        }
        //Validar que la Account de origen pertenezca al Client autenticado
        if(!(clientRepository.findByEmail(authentication.getName()).get().getAccounts().contains(account1.get()))) {
            return new ResponseEntity<>("Forbidden petition. The origin account belongs to another client", HttpStatus.FORBIDDEN);
        }
        //Valido que exista la Account de destino
        if(!accountRepository.findByNumber(toAccountNumber).isPresent()) {
            return new ResponseEntity<>("Forbidden petition. The destination account doesn´t exist", HttpStatus.FORBIDDEN);
        }
        //Valido que la Account de origen tenga el amount suficiente
        if(account1.get().getBalance() < amount) {
            return new ResponseEntity<>("Forbidden petition. The origin account´s balance is not enough for this transaction", HttpStatus.FORBIDDEN);
        }
        Account fromAccount = account1.get();
        Account toAccount = account2.get();
        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, amount, description, fromAccount);
        transactionRepository.save(transactionOrigin);
        Transaction transactionDestination = new Transaction(TransactionType.CREDIT, amount, description, toAccount);
        transactionRepository.save(transactionDestination);
        //Si toodo salió bien retorno un estado CREATED
        return new ResponseEntity<>("Transaction completed successfully", HttpStatus.CREATED);
    }

}
