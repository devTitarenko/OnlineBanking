package com.eisgroup.cb.dto;

import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.CardType;

import java.util.Date;

public class CardDTO {
    private CardType cardType;
    private Long cardNumber;
    private String cardHolder;
    private Date effectiveDate;
    private Date expirationDate;
    private String cvv2;
    private Account account;

    public CardDTO(){}

    public CardDTO(Card card){
        cardType=card.getCardType();
        cardNumber=card.getCardNumber();
        cardHolder=card.getCardHolder();
        effectiveDate=card.getEffectiveDate();
        expirationDate=card.getExpirationDate();
        cvv2=card.getCvv2();
        account=card.getAccount();
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

    @Override
    public String toString() {
        return "CardDTO{" +
                "cardType=" + cardType +
                ", cardNumber=" + cardNumber +
                ", cardHolder='" + cardHolder + '\'' +
                ", effectiveDate=" + effectiveDate +
                ", expirationDate=" + expirationDate +
                ", cvv2='" + cvv2 + '\'' +
                ", account=" + account +
                '}';
    }
}
