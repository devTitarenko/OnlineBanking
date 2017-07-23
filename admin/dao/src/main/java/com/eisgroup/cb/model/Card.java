package com.eisgroup.cb.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

@NamedQueries({
        @NamedQuery(name = "Card.updateExpDate",
                query = "UPDATE Card c SET c=:newExpDate WHERE c.cardNumber=:cardNumber"),
})

@Entity
@Table(name = "Card")
public class Card extends BaseObject implements PaymentMethod<Card>{

    public static final String UPDATE_EXPIRATION_DATE = "Card.updateExpDate";

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CardType cardType;

    @NotNull
    @Column
    private Long cardNumber;

    @NotNull
    @Column
    private String cardHolder;

    @NotNull
    @Column
    private Date effectiveDate;

    @NotNull
    @Column
    private Date expirationDate;

    @NotNull
    @Column
    private String cvv2;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @NotNull
    @Column
    private boolean isBlocked;

    public Status getStatus() {
        Status tempStatus = expirationDate.after(new Date()) && effectiveDate.before(new Date())
                ? Status.ACTIVE : Status.INACTIVE;
        if (tempStatus.equals(Status.ACTIVE)) {
            if (isBlocked()) {
                tempStatus = Status.BLOCKED;
            } else {
                tempStatus = Status.ACTIVE;
            }
        }
        return tempStatus;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

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

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + getId() +
                ", cardType=" + cardType +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", cvv2='" + cvv2 + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
    @Override
    public String getLabel() {
        Account cardAccount = getAccount();
        //Should String be used?
        StringBuilder stringBuilder = new StringBuilder(changeCardNumStyle(this.cardNumber)).append(", ");
        stringBuilder.append(this.cardType).append(", ").append(formatBalanceAmount(cardAccount.getBalance()))
                .append(" ").append(cardAccount.getCurrency());
        return stringBuilder.toString();
    }

    private String changeCardNumStyle(Long number) {
        StringBuilder str = new StringBuilder(String.valueOf(number));
        str.insert(4, " ");
        str.insert(9, " ");
        str.insert(14, " ");
        str.replace(7, 14, "** ****");
        return str.toString();
    }

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
        return getAccount();
    }

    @Override
    public PaymentType getPaymentsType() {
        return PaymentType.CARD;
    }
}
