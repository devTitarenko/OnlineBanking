package com.eisgroup.cb.dao.impl;

import com.eisgroup.cb.dao.CardDAO;
import com.eisgroup.cb.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class CardDAOImpl extends BaseObjectDAOImpl<Card> implements CardDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CardDAOImpl.class);

    public CardDAOImpl() {
        super(Card.class);
    }

    @Override
    public void setExpirationDate(Long cardNumber, Date newExpirationDate) {
        if (cardNumber == null || newExpirationDate == null) {
            LOG.debug("Can't execute update.  Card no: {} ; New date: {}", cardNumber, newExpirationDate);
            return;
        }
        LOG.debug("Updating the Expiration date of card no {} with new date: {}", cardNumber, newExpirationDate.toString());
        entityManager.createNamedQuery(Card.UPDATE_EXPIRATION_DATE)
                .setParameter("newExpDate", newExpirationDate)
                .setParameter("cardNumber", cardNumber)
                .executeUpdate();
    }

    @Override
    public Long getLastNumber() {
        return entityManager.createQuery(
                "SELECT cardNumber FROM Card WHERE id = (SELECT max(id) FROM Card)", Long.class).getSingleResult();
    }

    @Override
    public List<Card> getCardsByCustomerId(Long id) {
        return entityManager.createQuery(
                "SELECT c FROM Card c WHERE c.account.id IN" +
                        "(SELECT acc.id FROM Account acc WHERE acc.customer.id=:id)", Card.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List <Card> getCardByCardNumber(Long cardNumber) {
        return entityManager.createQuery("SELECT c FROM Card c WHERE cardNumber=:cardNumber", Card.class)
                .setParameter("cardNumber",cardNumber)
                .getResultList();
    }
}