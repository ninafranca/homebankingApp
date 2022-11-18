package com.minddhub.homebanking;

import com.minddhub.homebanking.models.*;
import com.minddhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	//@Bean: encapsula varios objetos en un único objeto
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			//Instancio los Clients y los guardo
			//En caso de configurar el role en el model de Client se lo agrego por parámetro
			//Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("hola1"), AuthorityUtils.createAuthorityList("CLIENT"));
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("hola1"));
			clientRepository.save(client1);
			//En caso de configurar el role en el model de Client se lo agrego por parámetro
			//Client client2 = new Client("Nina", "Spinelli", "ninafspin@gmail.com", passwordEncoder.encode("123"), AuthorityUtils.createAuthorityList("CLIENT"));
			Client client2 = new Client("Nina", "Spinelli", "ninafspin@gmail.com", passwordEncoder.encode("123"));
			clientRepository.save(client2);

			//Instancio las Accounts asociando su respectivo Client y las guardo
			Account account1 = new Account(AccountType.CURRENT, 5000.00, client1);
			accountRepository.save(account1);
			Account account2 = new Account(AccountType.SAVINGS, 7500.00, client1);
			accountRepository.save(account2);
			Account account3 = new Account(AccountType.SAVINGS, 2000.00, client2);
			accountRepository.save(account3);
			Account account4 = new Account(AccountType.CURRENT, 8000.00, client2);
			accountRepository.save(account4);

			//Instancio las Transactions asociando su respectiva Account y las guardo
			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1020.00, "varios", account1);
			transactionRepository.save(transaction1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 500.00, "varios", account1);
			transactionRepository.save(transaction2);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 600.00, "varios", account2);
			transactionRepository.save(transaction3);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 350.00, "varios", account4);
			transactionRepository.save(transaction4);

			//Guardo de nuevo las Accounts para que se refresque el balance después de las Transactions
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			//Instancio los Loans con su lista de cuantas cuotas acepta como pago
			Loan loan1 = new Loan("Hipotecario", 500000.00, List.of(12,24,36,48,60));
			loanRepository.save(loan1);
			Loan loan2 = new Loan("Personal", 100000.00, List.of(6,12,24));
			loanRepository.save(loan2);
			Loan loan3 = new Loan("Automotriz", 300000.00, List.of(6, 12, 24, 36));
			loanRepository.save(loan3);

			//Instancio los ClientLoans asociando sus respectivos Loan y Client
			ClientLoan clientLoan1 = new ClientLoan(60, 400000.00, loan1, client1);
			clientLoanRepository.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan(12, 50000.00, loan2, client1);
			clientLoanRepository.save(clientLoan2);
			ClientLoan clientLoan3 = new ClientLoan(24, 100000.00, loan2, client2);
			clientLoanRepository.save(clientLoan3);
			ClientLoan clientLoan4 = new ClientLoan(36, 200000.00, loan3, client2);
			clientLoanRepository.save(clientLoan4);

			//Instancio las Cards con sus respectivos Clients
			Card card1 = new Card(client1, CardType.CREDIT, CardColor.TITANIUM);
			cardRepository.save(card1);
			Card card2 = new Card(client1, CardType.DEBIT, CardColor.GOLD);
			cardRepository.save(card2);
			Card card3 = new Card(client2, CardType.CREDIT, CardColor.TITANIUM);
			cardRepository.save(card3);

		};

	}

}