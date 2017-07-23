package com.eisgroup.cb.dto;

import com.eisgroup.cb.model.Account;

import java.util.Date;

public class AccountDTO {

    private Account.Type type;

    private Long accountNumber;

    private Date validFrom;

    private Date validTo;

    private Account.Currency currency;

    private Double balance;

    private Double limit;

    public AccountDTO(Account account) {
        this.type = account.getType();
        this.accountNumber = account.getAccountNumber();
        this.validFrom = account.getValidFrom();
        this.validTo = account.getValidTo();
        this.currency = account.getCurrency();
        this.balance = account.getBalance();
        this.limit = account.getLimit();
    }

    public Account.Type getType() {
        return type;
    }

    public void setType(Account.Type type) {
        this.type = type;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Account.Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Account.Currency currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }
}
