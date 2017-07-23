package com.eisgroup.cb.model;

import java.util.Date;

public class SearchCriteria {

    private String firstNameCriteria;
    private String lastNameCriteria;
    private String mobileNumCriteria;
    private Long accountNumCriteria;
    private String accountTypeCriteria;
    private String cardTypeCriteria;
    private Status accountStatusCriteria;
    private Status cardStatusCriteria;
    private Date accountValidFrom;
    private Date accountValidTo;
    private Date accountValidFromEndPoint;
    private Date accountValidToEndPoint;

    public SearchCriteria() {
    }

    public boolean isEmpty() {
        return firstNameCriteria.length() == 0
                && lastNameCriteria.length() == 0
                && mobileNumCriteria == null
                && accountNumCriteria == null
                && accountTypeCriteria == null
                && cardTypeCriteria == null
                && accountValidFrom == null
                && accountValidTo == null;
    }

    public String getFirstNameCriteria() {
        return firstNameCriteria;
    }

    public void setFirstNameCriteria(String firstNameCriteria) {
        this.firstNameCriteria = firstNameCriteria;
    }

    public String getLastNameCriteria() {
        return lastNameCriteria;
    }

    public void setLastNameCriteria(String lastNameCriteria) {
        this.lastNameCriteria = lastNameCriteria;
    }

    public String getMobileNumCriteria() {
        return mobileNumCriteria;
    }

    public void setMobileNumCriteria(String mobileNumCriteria) {
        this.mobileNumCriteria = mobileNumCriteria;
    }

    public Long getAccountNumCriteria() {
        return accountNumCriteria;
    }

    public void setAccountNumCriteria(Long accountNumCriteria) {
        this.accountNumCriteria = accountNumCriteria;
    }

    public String getAccountTypeCriteria() { return accountTypeCriteria; }

    public void setAccountTypeCriteria(String accountTypeCriteria) { this.accountTypeCriteria = accountTypeCriteria; }

    public String getCardTypeCriteria() { return cardTypeCriteria; }

    public void setCardTypeCriteria(String cardTypeCriteria) { this.cardTypeCriteria = cardTypeCriteria; }

    public Status getAccountStatusCriteria() { return accountStatusCriteria; }

    public void setAccountStatusCriteria(Status accountStatusCriteria) { this.accountStatusCriteria = accountStatusCriteria; }

    public Status getCardStatusCriteria() { return cardStatusCriteria; }

    public void setCardStatusCriteria(Status cardStatusCriteria) { this.cardStatusCriteria = cardStatusCriteria; }

    public Date getAccountValidFrom() {
        return accountValidFrom;
    }

    public void setAccountValidFrom(Date accountValidFrom) {
        this.accountValidFrom = accountValidFrom;
    }

    public Date getAccountValidTo() {
        return accountValidTo;
    }

    public void setAccountValidTo(Date accountValidTo) {
        this.accountValidTo = accountValidTo;
    }

    public Date getAccountValidFromEndPoint() {
        return accountValidFromEndPoint;
    }

    public void setAccountValidFromEndPoint(Date accountValidFromEndPoint) {
        this.accountValidFromEndPoint = accountValidFromEndPoint;
    }

    public Date getAccountValidToEndPoint() {
        return accountValidToEndPoint;
    }

    public void setAccountValidToEndPoint(Date accountValidToEndPoint) {
        this.accountValidToEndPoint = accountValidToEndPoint;
    }

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "firstNameCriteria='" + firstNameCriteria + '\'' +
                ", lastNameCriteria='" + lastNameCriteria + '\'' +
                ", mobileNumCriteria='" + mobileNumCriteria + '\'' +
                ", accountNumCriteria=" + accountNumCriteria +
                ", accountTypeCriteria=" + accountTypeCriteria +
                ", cardTypeCriteria=" + cardTypeCriteria +
                ", accountStatusCriteria=" + accountStatusCriteria +
                ", cardStatusCriteria=" + cardStatusCriteria +
                ", accountValidFrom=" + accountValidFrom +
                ", accountValidTo=" + accountValidTo +
                '}';
    }
}