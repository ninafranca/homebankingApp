package com.minddhub.homebanking.dtos;

import com.minddhub.homebanking.models.Transaction;
import com.minddhub.homebanking.models.TransactionType;
import utils.TransactionUtils;

import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;
    private Double accountBalance;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = TransactionUtils.getDescriptionWithDate(transaction.getDescription(), transaction.getType(), transaction.getDate());
        this.date = transaction.getDate();
        this.accountBalance = transaction.getAccountBalance();
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

}
