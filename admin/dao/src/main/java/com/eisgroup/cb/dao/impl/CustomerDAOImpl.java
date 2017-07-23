package com.eisgroup.cb.dao.impl;

import com.eisgroup.cb.dao.CustomerDAO;
import com.eisgroup.cb.dto.CardReportDTO;
import com.eisgroup.cb.dto.CustomerSearchDto;
import com.eisgroup.cb.exceptions.PasswordDuplicateException;
import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.model.PasswordHistory;
import com.eisgroup.cb.model.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class CustomerDAOImpl extends BaseObjectDAOImpl<Customer> implements CustomerDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerDAOImpl.class);

    public CustomerDAOImpl() {
        super(Customer.class);
    }

    @Override
    public void save(Customer customer) {
        try {
            saveWithException(customer);
        } catch (PasswordDuplicateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveWithException(Customer customer) throws PasswordDuplicateException {
        Long id = customer.getId();
        String pass = customer.getPassword();
        if (id != null && isPasswordDuplicate(id, pass) && !pass.equals(customer.getPreviousPassword())) {
            throw new PasswordDuplicateException(
                    "This password = '" + pass + "' has already been used for this customer: " +
                            "customer id = " + id + ", " +
                            "customer last name = " + customer.getLastName());
        }

        super.save(customer);

        if (!isPasswordDuplicate(customer.getId(), pass)) {
            PasswordHistory passwordHistory = new PasswordHistory();
            passwordHistory.setPassword(pass);
            passwordHistory.setCustomerId(customer.getId());
            entityManager.persist(passwordHistory);
        }
    }

    @Override
    public Customer find(long id) {
        Customer customer = super.find(id);
        customer.setPreviousPassword(customer.getPassword());
        return customer;
    }

    @Override
    public List<Customer> getCustomerByMobileNum(Customer customer) {
        return entityManager
                .createQuery("SELECT a FROM Customer a WHERE a.mobileNum = :mobileNum AND a.password = :password", Customer.class)
                .setParameter("mobileNum", customer.getMobileNum())
                .setParameter("password", customer.getPassword())
                .getResultList();
    }

    @Override
    public List<CustomerSearchDto> findBySearchCriteria(SearchCriteria searchCriteria) {
        LOG.debug("Search was made by criteria - {}", searchCriteria);

        StringBuilder temp = new StringBuilder(
                "SELECT DISTINCT NEW com.eisgroup.cb.dto.CustomerSearchDto (c.id, c.firstName, c.lastName, c.mobileNum, " +
                        "(SELECT count(*) FROM c.accountList acc WHERE acc.validFrom < sysdate AND acc.validTo > sysdate)) " +
                        "FROM Customer c LEFT JOIN c.accountList ac " +
                        "WHERE (:firstName IS NULL OR LOWER(c.firstName) LIKE :firstName) " +
                        "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE :lastName) " +
                        "AND (:mobileNum IS NULL OR LOWER(c.mobileNum) LIKE :mobileNum) " +
                        "AND (:accountNumber IS NULL OR LOWER(ac.accountNumber) LIKE :accountNumber) ");

        if (searchCriteria.getAccountValidFrom() != null) {
            temp.append("AND (ac.validFrom between :validFrom AND :validFromEndPoint)");
        }
        if (searchCriteria.getAccountValidTo() != null) {
            temp.append("AND (ac.validTo between :validTo AND :validToEndPoint)");
        }

        Query namedQuery = entityManager.createQuery(temp.toString(), CustomerSearchDto.class)
                .setParameter("firstName", searchCriteria.getFirstNameCriteria() == null ?
                        null : "%" + searchCriteria.getFirstNameCriteria().trim().toLowerCase() + "%")
                .setParameter("lastName", searchCriteria.getLastNameCriteria() == null ?
                        null : "%" + searchCriteria.getLastNameCriteria().trim().toLowerCase() + "%")
                .setParameter("mobileNum", searchCriteria.getMobileNumCriteria() == null ?
                        null : "%" + searchCriteria.getMobileNumCriteria().trim().toLowerCase() + "%")
                .setParameter("accountNumber", searchCriteria.getAccountNumCriteria() == null ?
                        null : "%" + searchCriteria.getAccountNumCriteria().toString() + "%");

        if (searchCriteria.getAccountValidFrom() != null) {
            namedQuery.setParameter("validFrom", searchCriteria.getAccountValidFrom())
                    .setParameter("validFromEndPoint", searchCriteria.getAccountValidFromEndPoint());
        }

        if (searchCriteria.getAccountValidTo() != null) {
            namedQuery.setParameter("validTo", searchCriteria.getAccountValidTo())
                    .setParameter("validToEndPoint", searchCriteria.getAccountValidToEndPoint());
        }

        return namedQuery.getResultList();
    }

    @Override
    public List<CardReportDTO> findReportsByCriteria(SearchCriteria searchCriteria) {
        LOG.debug("Search was made by criteria - {}", searchCriteria);

        StringBuilder temp = new StringBuilder(
                "SELECT NEW com.eisgroup.cb.dto.CardReportDTO (ac.accountNumber, ac.type, card.cardNumber, " +
                        "card.cardType, card.cardHolder, c.firstName, c.lastName, card.effectiveDate, " +
                        "card.expirationDate, ac.validTo, ac.validFrom, ac.balance, ac.currency) " +
                        "FROM Customer c LEFT JOIN c.accountList ac LEFT JOIN ac.cards card " +
                        "WHERE (:firstName IS NULL OR LOWER(c.firstName) LIKE :firstName) " +
                        "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE :lastName) " +
                        "AND (:accountType IS NULL OR LOWER(ac.type) LIKE LOWER(:accountType)) " +
                        "AND (:cardType IS NULL OR LOWER(card.cardType) LIKE LOWER(:cardType)) ");

        if (searchCriteria.getAccountValidFrom() != null) {
            temp.append("AND (ac.validFrom between :validFrom AND :validFromEndPoint)");
        }
        if (searchCriteria.getAccountValidTo() != null) {
            temp.append("AND (ac.validTo between :validTo AND :validToEndPoint)");
        }
        Query namedQuery = entityManager.createQuery(temp.toString(), CardReportDTO.class)
                .setParameter("firstName", searchCriteria.getFirstNameCriteria() == null ?
                        null : "%" + searchCriteria.getFirstNameCriteria().trim().toLowerCase() + "%")
                .setParameter("lastName", searchCriteria.getLastNameCriteria() == null ?
                        null : "%" + searchCriteria.getLastNameCriteria().trim().toLowerCase() + "%")
                .setParameter("accountType", searchCriteria.getAccountTypeCriteria())
                .setParameter("cardType", searchCriteria.getCardTypeCriteria());

        if (searchCriteria.getAccountValidFrom() != null) {
            namedQuery.setParameter("validFrom", searchCriteria.getAccountValidFrom())
                    .setParameter("validFromEndPoint", searchCriteria.getAccountValidFromEndPoint());
        }

        if (searchCriteria.getAccountValidTo() != null) {
            namedQuery.setParameter("validTo", searchCriteria.getAccountValidTo())
                    .setParameter("validToEndPoint", searchCriteria.getAccountValidToEndPoint());
        }
        return namedQuery.getResultList();
    }

    @Override
    public List<Customer> findByTin(String tin) {
        LOG.debug("Search customers by tin - {}", tin);
        return entityManager.createNamedQuery(Customer.FIND_BY_TIN, Customer.class)
                .setParameter("tin", tin.trim())
                .getResultList();
    }

    @Override
    public List<Customer> findByMobileNum(String mobileNum) {
        LOG.debug("Search customers by mobileNum - {}", mobileNum);
        return entityManager.createNamedQuery(Customer.FIND_BY_MOBILENUM, Customer.class)
                .setParameter("mobileNum", mobileNum.trim())
                .getResultList();
    }

    @Override
    public boolean isPasswordDuplicate(Long customerId, String password) {
        return !entityManager.createQuery(
                "SELECT 1 FROM PasswordHistory WHERE customerId = :customerId AND password = :password")
                .setParameter("customerId", customerId)
                .setParameter("password", password)
                .getResultList().isEmpty();
    }

    @Override
    public List<Customer> getCustomerByAccountNumber(Long accountNumber) {
        return entityManager
                .createQuery("SELECT c FROM Customer c INNER JOIN c.accountList as a " +
                        "WHERE a.accountNumber=:accNum", Customer.class)
                .setParameter("accNum", accountNumber)
                .getResultList();
    }
}