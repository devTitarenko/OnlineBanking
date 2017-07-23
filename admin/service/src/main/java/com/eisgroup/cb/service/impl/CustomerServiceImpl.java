package com.eisgroup.cb.service.impl;

import com.eisgroup.cb.dao.CustomerDAO;
import com.eisgroup.cb.dto.CardReportDTO;
import com.eisgroup.cb.dto.CustomerSearchDto;
import com.eisgroup.cb.exceptions.PasswordDuplicateException;
import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.model.SearchCriteria;
import com.eisgroup.cb.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerDAO dao;

    @Autowired
    public CustomerServiceImpl(CustomerDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<CustomerSearchDto> findBySearchCriteria(SearchCriteria searchCriteria) {
        if (searchCriteria.isEmpty()) {
            LOG.warn("Get empty searchCriteria");
            return Collections.emptyList();
        }

        LOG.info("Trying to search with " + searchCriteria.toString());
        List<CustomerSearchDto> customers = dao.findBySearchCriteria(searchCriteria);
        LOG.debug("Returned list is empty?: {}", customers.isEmpty());
        return customers;
    }

    @Override
    public List<CardReportDTO> findReportsByCriteria(SearchCriteria searchCriteria) {
        if (searchCriteria.isEmpty()) {
            LOG.warn("Get empty searchCriteria");
            return Collections.emptyList();
        }

        LOG.info("Trying to search with " + searchCriteria.toString());
        List<CardReportDTO> reportsList = dao.findReportsByCriteria(searchCriteria);
        LOG.debug("Returned list is empty?: {}", reportsList.isEmpty());
        return reportsList;
    }

    @Override
    public void save(Customer customer) throws PasswordDuplicateException {
        dao.saveWithException(customer);
    }

    @Override
    public boolean isCustomerValid(Customer customer) {
        LOG.info("Attempt to enter with mobileNum - {}", customer.getMobileNum());
        List<Customer> customersList = dao.getCustomerByMobileNum(customer);

        if (customersList.size() > 1) {
            LOG.error("Table Customer not consistent, {} equal records", customersList.size());
            throw new NonUniqueResultException("Table Customer not consistent, " + customersList.size() + " equal records");
        }
        return !customersList.isEmpty();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return dao.find(id);
    }

    @Override
    public boolean isTinExists(Customer customer) {
        String tin = customer.getTin();
        if (tin != null) {
            LOG.info("Checking for tin - {}", tin);
            List<Customer> customerList = dao.findByTin(tin);

            int listSize = customerList.size();
            if (listSize > 1) {
                LOG.error("Table Customer not consistent, {} equal TIN records", customerList.size());
                throw new NonUniqueResultException("Table Customer not consistent, " + customerList.size() + " equal TIN records");
            } else if (listSize == 1) {
                return customer.getId() == null || !customer.getId().equals(customerList.get(0).getId());
            }
        }
        return false;
    }

    @Override
    public boolean isMobileNumExists(Customer customer) {
        String mobile = customer.getMobileNum();
        if (mobile != null) {
            LOG.info("Checking for mobile number - {}", mobile);
            List<Customer> customerList = dao.findByMobileNum(customer.getMobileNum());

            int listSize = customerList.size();
            if (listSize > 1) {
                LOG.error("Table Customer not consistent, {} equal mobileNum records", customerList.size());
                throw new NonUniqueResultException("Table Customer not consistent, " + customerList.size() + " equal mobileNum records");
            } else if (listSize == 1) {
                return customer.getId() == null || !customer.getId().equals(customerList.get(0).getId());
            }
        }
        return false;
    }

    @Override
    public List<Account> getCustomerAccountsByCustId(Long id) {
        Customer customer = getCustomerById(id);
        return customer.getAccountList();
    }

    @Override
    public boolean isPasswordDuplicate(Long customerId, String password) {
        return !(customerId == null || password == null) && dao.isPasswordDuplicate(customerId, password);
    }

    @Override
    public Customer getCustomerByMobileNumber(String mobileNumber) {
        return dao.findByMobileNum(mobileNumber).get(0);
    }

    @Override
    public List<Card> getCustomerCardsByCustId(Long id) {
        List<Account> accountList = getCustomerAccountsByCustId(id);
        List<Card> cardList = new ArrayList<>();
        if (accountList != null) {
            for (Account account : accountList) {
                if (account.getCards() != null) {
                    cardList.addAll(account.getCards());
                }
            }
            return cardList;
        }
        return cardList;
    }

    @Override
    public boolean isNumberAndTinValid(Long cardOrAccountNumber, String tin) {
        List<Customer> customerList = dao.findByTin(tin);
        Customer customer = customerList.isEmpty() ? null : customerList.get(0);
        LOG.info("customer", customer);
        if (customer == null)
            return false;

        List<Long> longs = new ArrayList<>();
        longs.addAll(customer.getAccountList().stream()
                .map(Account::getAccountNumber).collect(Collectors.toList()));
        customer.getAccountList().forEach(account ->
                longs.addAll(account.getCards().stream()
                        .map(Card::getCardNumber).collect(Collectors.toList())));
        return longs.contains(cardOrAccountNumber);
    }

    @Override
    public Customer findCustomer(Long number, String tin) {
        List<Customer> customerList = dao.findByTin(tin);
        Customer customer = customerList.isEmpty() ? null : customerList.get(0);
        LOG.info("customer", customer);
        if (customer == null)
            return null;

        List<Long> longs = new ArrayList<>();
        longs.addAll(customer.getAccountList().stream()
                .map(Account::getAccountNumber).collect(Collectors.toList()));
        customer.getAccountList().forEach(account ->
                longs.addAll(account.getCards().stream()
                        .map(Card::getCardNumber).collect(Collectors.toList())));
        return longs.contains(number) ? customer : null;

        /*if (customer != null) {
            Integer numberLength = (number + "").length();
            List<Account> accountList = customer.getAccountList();
            if (numberLength == 14) {
                for (Account account : accountList) {
                    if (Objects.equals(account.getAccountNumber(), number)) {
                        return customer;
                    }
                }
            }
            if (numberLength == 16) {
                for (Account account : accountList) {
                    for (Card card : account.getCards()) {
                        if (Objects.equals(card.getCardNumber(), number)) {
                            return customer;
                        }
                    }
                }
            }
        }
        return null;*/
    }

    @Override
    public Customer findByAccountNumber(Long accountNumber) {
        if (accountNumber != null) {
            LOG.info("Checking for account number ", accountNumber);
            List<Customer> customerList = dao.getCustomerByAccountNumber(accountNumber);

            int listSize = customerList.size();
            if (listSize > 1) {
                LOG.error("Table Customer not consistent", customerList.size());
                throw new NonUniqueResultException("Table Customer not consistent, " + customerList.size() + " equal account number records");
            } else if (listSize == 1) {
                return customerList.get(0);
            }
        }
        return null;
    }
}