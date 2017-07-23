package com.eisgroup.cb.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "TransferRecord")
public class TransferRecord extends BaseObject implements Serializable {

    @NotNull
    private Date transactionDateAndTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionSubtype transactionSubtype;

    @NotNull
    private Long creditAccountNumber;

    @NotNull
    private Long debitAccountNumber;

    @NotNull
    private double transactionAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Account.Currency transactionCurrency;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Location transactionLocation;

    @NotNull
    private Long performerId;

    @NotNull
    private double startingBalance;

    @NotNull
    private double endingBalance;

    private Long cardFromNumber;

    private Long cardToNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EntryType entryType;



    public enum TransactionType {
        MONEY_TRANSFER("Money transfer");

        private final String name;

        TransactionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Location {
        ONLINE_BANKING("Online banking");

        private final String name;

        Location (String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum TransactionSubtype {
        BETWEEN_ACCOUNTS("Between own accounts"),
        WITHIN_BANK("Within Bank");

        private final String name;

        TransactionSubtype(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum EntryType {
        CREDIT("Credit"),
        DEBIT("Debit");

        private final String name;

        EntryType (String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public Date getTransactionDateAndTime() {
        return transactionDateAndTime;
    }

    public void setTransactionDateAndTime(Date transactionDateAndTime) {
        this.transactionDateAndTime = transactionDateAndTime;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionSubtype getTransactionSubtype() {
        return transactionSubtype;
    }

    public void setTransactionSubtype(TransactionSubtype transactionSubtype) {
        this.transactionSubtype = transactionSubtype;
    }

    public Long getCreditAccountNumber() {
        return creditAccountNumber;
    }

    public void setCreditAccountNumber(Long creditAccountNumber) {
        this.creditAccountNumber = creditAccountNumber;
    }

    public Long getDebitAccountNumber() {
        return debitAccountNumber;
    }

    public void setDebitAccountNumber(Long debitAccountNumber) {
        this.debitAccountNumber = debitAccountNumber;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Account.Currency getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(Account.Currency transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public Location getTransactionLocation() {
        return transactionLocation;
    }

    public void setTransactionLocation(Location transactionLocation) {
        this.transactionLocation = transactionLocation;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public double getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(double startingBalance) {
        this.startingBalance = startingBalance;
    }

    public double getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(double endingBalance) {
        this.endingBalance = endingBalance;
    }

    public Long getCardFromNumber() {
        return cardFromNumber;
    }

    public void setCardFromNumber(Long cardFromNumber) {
        this.cardFromNumber = cardFromNumber;
    }

    public Long getCardToNumber() {
        return cardToNumber;
    }

    public void setCardToNumber(Long cardToNumber) {
        this.cardToNumber = cardToNumber;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    @Override
    public String toString() {
        return "TransferRecord{" +
                "transactionDateAndTime=" + transactionDateAndTime +
                ", transactionType=" + transactionType +
                ", transactionSubtype=" + transactionSubtype +
                ", creditAccountNumber=" + creditAccountNumber +
                ", debitAccountNumber=" + debitAccountNumber +
                ", transactionAmount=" + transactionAmount +
                ", transactionCurrency=" + transactionCurrency +
                ", transactionLocation=" + transactionLocation +
                ", performerId=" + performerId +
                ", startingBalance=" + startingBalance +
                ", endingBalance=" + endingBalance +
                ", cardFromNumber=" + cardFromNumber +
                ", cardToNumber=" + cardToNumber +
                ", entryType=" + entryType +
                '}';
    }
}
