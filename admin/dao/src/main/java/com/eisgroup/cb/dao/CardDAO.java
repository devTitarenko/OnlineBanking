package com.eisgroup.cb.dao;

import com.eisgroup.cb.model.Card;

import java.util.Date;
import java.util.List;

public interface CardDAO extends BaseObjectDAO<Card>, LastNumberGetter {

    void setExpirationDate(Long cardNumber, Date newExpirationDate);
    List<Card> getCardsByCustomerId(Long id);
    List <Card> getCardByCardNumber(Long cardNumber);
}