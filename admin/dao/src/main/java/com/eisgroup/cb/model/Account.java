package com.eisgroup.cb.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "Account")
public class Account extends BaseObject implements Serializable, PaymentMethod<Account> {

    @NotNull
    @Enumerated(EnumType.STRING)
    private Type type;

    @NotNull
    private Long accountNumber;

    @NotNull
    private Date validFrom;

    @NotNull
    private Date validTo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @NotNull
    private Double balance;

    @NotNull
    private Double limit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    private List<Card> cards;

    @Version
    private int version;

    public enum Type {
        TRANSACTIONAL("Transactional account"),
        LOAN("Loan account"),
        SAVINGS("Savings account"),
        PAYMENT("Payment card account");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Currency {
        UAH("UAH"),
        USD("USD"),
        EUR("EUR");

        private final String name;

        Currency(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    //    Getters and Setters
    public Status getStatus() {
        return validTo != null && validFrom != null &&
                validTo.after(new Date()) && validFrom.before(new Date())
                ? Status.ACTIVE : Status.INACTIVE;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + getId() +
                ", type=" + type +
                ", accountNumber=" + accountNumber +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", currency=" + currency +
                ", balance=" + balance +
                ", limit=" + limit +
                ", status='" + getStatus() + '\'' +
                '}';
    }

    @Override
    public String getLabel() {
        StringBuilder stringBuilder = new StringBuilder(this.accountNumber.toString()).append(", ");
        stringBuilder.append(this.type).append(", ").append(formatBalanceAmount(this.balance)).append(" ")
                .append(this.currency);
        return stringBuilder.toString();
    }
    //util
    private String formatBalanceAmount(Double value) {
        //can we use Locale?
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(value);
    }

    @Override
    public Account getPaymentAccount() {
        return this;
    }


    @Override
    public PaymentType getPaymentsType() {
        return PaymentType.ACCOUNT;
    }
}