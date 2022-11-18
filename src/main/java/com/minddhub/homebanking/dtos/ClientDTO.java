package com.minddhub.homebanking.dtos;

import com.minddhub.homebanking.models.Client;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    //Asocio las Accounts relacionadas al Client
    private Set<AccountDTO> accounts;
    //Asocio los ClientLoans relacionados al Client
    public Set<ClientLoanDTO> loans = new HashSet<>();
    //Asocio las Cards relacionadas al Client
    public Set<CardDTO> cards = new HashSet<>();

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.password = client.getPassword();
        //Obtengo las Accounts asociadas al Client
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        //Obtengo los ClientLoans asociados al Client
        this.loans = client.getLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toSet());
        //Obtengo las Cards asociadas al Client
        this.cards = client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public void setLoans(Set<ClientLoanDTO> loans) {
        this.loans = loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public void setCards(Set<CardDTO> cards) {
        this.cards = cards;
    }

}
