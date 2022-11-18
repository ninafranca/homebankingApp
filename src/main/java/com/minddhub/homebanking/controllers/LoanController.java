package com.minddhub.homebanking.controllers;

import com.minddhub.homebanking.dtos.LoanApplicationDTO;
import com.minddhub.homebanking.dtos.LoanDTO;
import com.minddhub.homebanking.models.*;
import com.minddhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
//Defino la ruta base
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    //GET para todos los Loans
    @GetMapping("/loans")
    public List<LoanDTO> getAccounts(){
        //stream() procesa y transforma cada elemento de la lista que retorna findAll()
        //y map() ejecuta el método constructor que está en AccountDTO
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }

    //GET de un Loan por ID
    @GetMapping("/loans/{id}")
    public LoanDTO getLoan(@PathVariable Long id) {
        return loanRepository.findById(id).map(LoanDTO::new).orElse(null);
    }

    //Creo un ClientLoan a partir de la data tomada del LoanApplicationDTO
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createClientLoan(@RequestBody LoanApplicationDTO loanApplicationDTO,
                                                   Authentication authentication) {
        //Establezco los Optional: Loan, Account y Client que voy a usar
        Optional<Loan> optionalLoan = loanRepository.findById(loanApplicationDTO.getLoanId());
        Optional<Account> optionalAccount = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        Optional<Client> optionalClient = clientRepository.findByEmail(authentication.getName());
        //Valido que no falten datos
        if(loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0 || loanApplicationDTO.getToAccountNumber().isEmpty() || loanApplicationDTO.getLoanId() == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //Valido que el Loan exista en la DB
        if(optionalLoan.isEmpty()) {
            return new ResponseEntity<>("Inexistent loan", HttpStatus.FORBIDDEN);
        }
        Loan loan = optionalLoan.get();
        Client client = optionalClient.get();
        //Valido que el amount que están solicitando sea menor o igual al máximo permitido
        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Exceeds maximum amount possible", HttpStatus.FORBIDDEN);
        }
        //Valido que exista la cantidad de payments que se están pidiendo dentro de las permitidas en el Loan
        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Payment not permitted", HttpStatus.FORBIDDEN);
        }
        //Valido que la Account exista en la DB
        if(optionalAccount.isEmpty()) {
            return new ResponseEntity<>("Inexistent account", HttpStatus.FORBIDDEN);
        }
        Account account = optionalAccount.get();
        //Valido que el Client sea el mismo que está autenticado
        if(!authentication.getName().equals(account.getClient().getEmail())) {
            return new ResponseEntity<>("Not authorized", HttpStatus.FORBIDDEN);
        }
        //Guardo la Transaction del Loan
        transactionRepository.save(new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan approved", account));
        //Guardo el ClientLoan
        clientLoanRepository.save(new ClientLoan(loanApplicationDTO.getPayments(), loanApplicationDTO.getAmount() * 1.2, loan, client));
        return new ResponseEntity<>("Loan granted successfully", HttpStatus.OK);
    }

}
