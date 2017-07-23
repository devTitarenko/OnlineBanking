package com.eisgroup.cb.service.impl;

import com.eisgroup.cb.dao.CardDAO;
import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.CardType;
import com.eisgroup.cb.model.Status;
import com.eisgroup.cb.service.AbstractNumberGenerator;
import com.eisgroup.cb.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("cardService")
public class CardServiceImpl extends AbstractNumberGenerator implements CardService {
    private static final Logger LOG = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardDAO dao;

    @Autowired
    public CardServiceImpl(CardDAO dao) {
        super(dao, 8);
        this.dao = dao;
    }

    @Override
    public void save(Card card) {
        LOG.debug("saving card - {}", card);
        dao.save(card);
    }

    @Override
    public Long generateCardNumber(CardType type) {
        Long cardNumber = getNext();
        switch (type) {
            case VISA:
                cardNumber += 4211011100000000L;
                break;
            case MASTERCARD:
                cardNumber += 4422011100000000L;
                break;
            case MAESTRO:
                cardNumber += 4644011100000000L;
                break;
            default:
                cardNumber = null;
        }
        LOG.debug("Generated Card Number is: {}", cardNumber);
        return cardNumber;
    }

    @Override
    public Card findByCardNumber(Long cardNumber) {
        List<Card> cards = dao.getCardByCardNumber(cardNumber);
        int listSize = cards.size();
        if (listSize > 1) {
            throw new NonUniqueResultException("Table Card not consistent");
        } else if (listSize == 1) {
            LOG.info("Found card with card number " + cardNumber);
            return cards.get(0);
        }
        LOG.info("Card with card number "+cardNumber+" is not found");
        return null;
    }

    @Override
    public List<Card> getCardsByCustomerId(Long id) {
        return dao.getCardsByCustomerId(id);
    }

    @Override
    public List<Card> filterByStatus(List<Card> cards, Status status) {
        return cards.stream().filter(card ->
                card.getStatus().equals(status)).collect(Collectors.toList());
    }
}