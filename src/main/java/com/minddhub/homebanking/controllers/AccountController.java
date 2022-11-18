package com.minddhub.homebanking.controllers;

import com.minddhub.homebanking.models.*;
import com.minddhub.homebanking.repositories.AccountRepository;
import com.minddhub.homebanking.dtos.AccountDTO;
import com.minddhub.homebanking.repositories.ClientRepository;
import com.minddhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
//Defino la ruta base
@RequestMapping("/api")
public class AccountController {

    //@Autowired: permite inyectar unas dependencias con otras
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    //Defino la ruta donde hacer el GET de Accounts después de la ruta base
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        //stream() procesa y transforma cada elemento de la lista que retorna findAll()
        //y map() ejecuta el método constructor que está en AccountDTO
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    //Hago GET de una Account por ID
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    //Hago GET de una Account por Balance mayor a
    @GetMapping("/accounts/balanceGreaterThan")
    public List<AccountDTO> getAccountByBalanceGreaterThan(@RequestParam double balance) {
        return accountRepository.findByBalanceGreaterThan(balance).stream().map(AccountDTO::new).collect(toList());
    }

    //GET de Accounts creas antes de X fecha
    @GetMapping("/accounts/dateBefore")
    public List<AccountDTO> findByCreationDateStartDateBefore(@RequestParam String creationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return accountRepository.findByCreationDateBefore(LocalDateTime.parse(creationDate, formatter)).stream().map(AccountDTO::new).collect(toList());
    }

    //Hago GET de una Account por ID
    @GetMapping("/accounts/number")
    public AccountDTO getByNumber(@RequestParam String number) {
        return accountRepository.findByNumber(number).map(AccountDTO::new).orElse(null);
    }

    //GET de un Client por email
    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getClientAccounts(Authentication authentication) {
        return clientRepository.findByEmail(authentication.getName()).get().getAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    //Creo una Account con un POST
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        try {
            //Busco el Client por email, si existe valido que tenga menos de 3 Accounts y creo una nueva
            Optional<Client> client = clientRepository.findByEmail(authentication.getName());
            if(client.isPresent()) {
                if (client.get().getAccounts().stream().count() >= 3) {
                    return new ResponseEntity<>("Maximum number of accounts reached", HttpStatus.FORBIDDEN);
                }
                //Creo la nueva Account validando que el number no se repita en la DB
                Account account;
                do {
                    //Le paso el AccountType como CURRENT por defecto a la nueva instancia de Account
                    account = new Account(AccountType.CURRENT, 0.00, client.get());
                } while(accountRepository.findByNumber(account.getNumber()).isPresent());
                accountRepository.save(account);
                return new ResponseEntity<>("New account created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Client not found", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return new ResponseEntity<>("Error creating account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Creo una Account con un POST y le paso el AccountType para que no sea CURRENT por defecto
    @PostMapping("/clients/current/accounts/accountType")
    public ResponseEntity<Object> createAccountWithAccountType(Authentication authentication,
                                                               @RequestParam AccountType accountType) {
        try {
            //Busco el Client por email, si existe valido que tenga menos de 3 Accounts y creo una nueva
            Optional<Client> client = clientRepository.findByEmail(authentication.getName());
            if(client.isPresent()) {
                if (client.get().getAccounts().stream().count() >= 3) {
                    return new ResponseEntity<>("Number of maximum accounts reached", HttpStatus.FORBIDDEN);
                }
                //Creo la nueva Account validando que el number no se repita en la DB
                Account account;
                do {
                    //Le paso el AccountType como CURRENT a la nueva instancia de Account
                    account = new Account(accountType, 0.00, client.get());
                } while(accountRepository.findByNumber(account.getNumber()).isPresent());
                accountRepository.save(account);
                return new ResponseEntity<>("New account created", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Client not found", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return new ResponseEntity<>("Error creating account", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE de una Account por ID
    //con cascade = CascadeType.REMOVE en el Set de Transactions del Account model elimino
    //todas las Transactions relacionadas a la Account
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Object> deleteAccountAndTransactions(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
