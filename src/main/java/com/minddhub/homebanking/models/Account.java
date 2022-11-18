package com.minddhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import utils.AccountUtils;
import utils.CardUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//Creo la Entity de Account en la DB
@Entity
public class Account {

    //Genero el ID de manera automática
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;

    //Evita la recursividad que se crea entre Client y Account en caso de no crear un DTO
    //@JsonIgnore
    //Defino que varias Accounts pueden tener un Client asociado
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    //Relaciono las Transactions asociadas
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Transaction> transactions = new HashSet<>();

    public Account() {
    }

    public Account(AccountType accountType, double balance, Client client) {
        //Creo un número al azar en generateRandomNumber() (en este caso entre 3 y 8) y se lo paso
        //por parámetro a generateRandomNumbers(el_numero_al_azar_aca) para generar esa cantidad
        //de números para la Account
        this.number = "VIN-" + CardUtils.generateRandomNumbers(AccountUtils.generateRandomNumber(3, 9));
        this.creationDate = LocalDateTime.now();
        this.accountType = accountType;
        this.balance = balance;
        this.client = client;
    }

    public Long getNro() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

}