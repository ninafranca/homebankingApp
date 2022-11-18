package com.minddhub.homebanking;

import com.minddhub.homebanking.models.*;
import com.minddhub.homebanking.repositories.CardRepository;
import com.minddhub.homebanking.repositories.ClientRepository;
import com.minddhub.homebanking.repositories.LoanRepository;
import com.minddhub.homebanking.repositories.TransactionRepository;
import org.hamcrest.text.CharSequenceLength;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import utils.CardUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest

@AutoConfigureTestDatabase(replace = NONE)

public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    //Client Tests
    @Test
    public void existsClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void existsMelba() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));
    }

    @Test
    public void mailHasAt() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, everyItem(hasProperty("email", containsString("@"))));
    }

    @Test

    public void existClientsEmail(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, everyItem(hasProperty("email", not(emptyString()))));
    }

    //Card Tests
    @Test
    public void existsCard() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, everyItem(hasProperty("type", is(not(nullValue())))));
    }

    @Test
    public void cardCvvIsCreated() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, everyItem(hasProperty("cvv", is(not(nullValue())))));
    }

    @Test
    public void existsCardColorGold() {
        List<Card> card = cardRepository.findAll();
        assertThat(card, hasItem(hasProperty("color", is(CardColor.GOLD))));
    }

    @Test
    public void cardNumberIsCreated() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, everyItem(hasProperty("number", is(not(nullValue())))));
    }

    @Test
    public void generateRandomNWorks() {
        String cardNumber = CardUtils.generateRandomNumbers(3);
        assertThat(cardNumber, CharSequenceLength.hasLength(6));
    }

    //Transaction Tests
    @Test
    public void existsTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    @Test
    public void existsTransactionType() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, everyItem(hasProperty("account", is(not(nullValue())))));
    }

}
