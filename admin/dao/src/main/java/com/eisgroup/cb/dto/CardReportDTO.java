package com.eisgroup.cb.dto;

import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.CardType;
import com.eisgroup.cb.model.Status;

import java.util.Date;

public class CardReportDTO {

    private Long accountNumber;

    private Account.Type accountType;

    private Long cardNumber;

    private CardType cardType;

    private String cardholder;

    private String firstName;

    private String lastName;

    private Date validTo;

    private Date validFrom;

    private Date effectiveDate;

    private Date expirationDate;

    private Double balance;

    private Account.Currency currency;

    public CardReportDTO(Long accountNumber, Account.Type accountType, Long cardNumber, CardType cardType,
                         String cardholder, String firstName, String lastName, Date effectiveDate, Date expirationDate,
                         Date validTo, Date validFrom, Double balance, Account.Currency currency) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardholder = cardholder;
        this.firstName = firstName;
        this.lastName = lastName;
        this.validTo = validTo;
        this.validFrom = validFrom;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.balance = balance;
        this.currency = currency;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Account.Type getAccountType() {
        return accountType;
    }

    public void setAccountType(Account.Type accountType) {
        this.accountType = accountType;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholderName) {
        this.cardholder = cardholderName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getValidTo() { return validTo; }

    public void setValidTo(Date validTo) { this.validTo = validTo; }

    public Date getValidFrom() { return validFrom; }

    public void setValidFrom(Date validFrom) { this.validFrom = validFrom; }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Account.Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Account.Currency currency) {
        this.currency = currency;
    }

    public Status getAccountStatus() {

        return dateIsNull(validTo).after(new Date()) && dateIsNull(validFrom).before(new Date())
                ? Status.ACTIVE : Status.INACTIVE;
    }

    public Status getCardStatus() {

        Status tempStatus = dateIsNull(expirationDate).after(new Date()) && dateIsNull(effectiveDate).before(new Date())
                ? Status.ACTIVE : Status.INACTIVE;
        return tempStatus;
    }

    public Date dateIsNull(Date date) {
        Date yesterday = new Date(new Date().getTime() - (1000 * 60 * 60 * 24));
        return String.valueOf(date).equals("null") ? yesterday : date;
    }

    public String getCustomerName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() { return "CardReportDTO{ " +
            "accountNumber=" + accountNumber +
            ", accountType=" + accountType +
            ", cardNumber='" + cardNumber + '\'' +
            ", cardType='" + cardType + '\'' +
            ", cardholderName='" + cardholder + '\'' +
            ", customerFirstName='" + firstName + '\'' +
            ", customerLastName='" + lastName + '\'' +
            ", ValidTo='" + validTo + '\'' +
            ", ValidFrom='" + validFrom + '\'' +
            ", effectiveDate=" + effectiveDate +
            ", expirationDate=" + expirationDate +
            ", balance='" + balance + '\'' +
            ", currency=" + currency +
            '}';
    }
}
