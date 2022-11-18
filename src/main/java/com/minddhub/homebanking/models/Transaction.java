package com.minddhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import utils.AccountUtils;
import utils.TransactionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

//Creo la Entidad de Transaction en la DB
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    //Relación muchas a una entre Transactions y Account
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    private Double accountBalance;

    public Transaction() {
    }

    public Transaction(TransactionType type, double amount, String description, Account account) {
        this.type = type;
        //Devuelve si la Transaction es de tipo crédito o débito que configuré en TransactionUtils
        this.amount = TransactionUtils.getAmountWithType(type, amount);
        this.description = description;
        this.date = LocalDateTime.now();
        //Obtengo la Account asociada
        this.account = account;
        //Actualizo el monto del Balance en Account después de las Transactions asociadas según sus valores
        AccountUtils.updateBalance(this.account, this.amount);
        //Obtengo el balance actualizado de la Account
        this.accountBalance = account.getBalance();
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

}