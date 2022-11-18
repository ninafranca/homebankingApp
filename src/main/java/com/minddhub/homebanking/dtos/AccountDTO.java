package com.minddhub.homebanking.dtos;

import com.minddhub.homebanking.models.Account;
import com.minddhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

//Creo el DTO de Account que devuelve solo la información que le pido
public class AccountDTO {

    private Long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;
    //Le asocio las Transactions relacionadas a ésta Account
    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account) {
        this.id = account.getNro();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
        //Me trae todas las Transactions asociadas a la Account
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionDTO> accounts) {
        this.transactions = transactions;
    }

}