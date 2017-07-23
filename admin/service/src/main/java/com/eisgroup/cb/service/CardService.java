package com.eisgroup.cb.service;

import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.CardType;
import com.eisgroup.cb.model.Status;

import java.util.List;

public interface CardService {
    void save(Card card);

    Long generateCardNumber(CardType type);

    List<Card> getCardsByCustomerId(Long id);

    List<Card> filterByStatus(List<Card> cards, Status status);

    Card findByCardNumber(Long cardNumber);
}