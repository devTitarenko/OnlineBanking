package com.eisgroup.cb.service.impl;

import com.eisgroup.cb.dao.AccountDao;
import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.Status;
import com.eisgroup.cb.service.AbstractNumberGenerator;
import com.eisgroup.cb.service.AccountService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "accountService")
public class AccountServiceImpl extends AbstractNumberGenerator implements AccountService {

    private final AccountDao accountDao;

    private static final Logger LOGGER = Logger.getLogger(AccountService.class);

    @Autowired
    public AccountServiceImpl(AccountDao accountDao) {
        super(accountDao, 6);
        this.accountDao = accountDao;
    }

    @Override
    public void save(Account account) {
        LOGGER.info("SAVING... - " + account);
        accountDao.save(account);
    }

    @Override
    public Account findById(Long id) {
        Account account = accountDao.find(id);
        if (account != null) {
            LOGGER.info("Found 1 account with id=" + id + ", :" + account);
        } else {
            LOGGER.info("There are no existing accounts with id=" + id);
        }
        return account;
    }

    @Override
    public Long generateAccountNumber(Account.Type type) {
        Long accountNumber = getNext();
        switch (type) {
            case LOAN:
                accountNumber += 26252111000000L;
                break;
            case PAYMENT:
                accountNumber += 26251111000000L;
                break;
            case SAVINGS:
                accountNumber += 26300111000000L;
                break;
            case TRANSACTIONAL:
                accountNumber += 26220111000000L;
                break;
            default:
                accountNumber = null;
        }
        LOGGER.info("Generated Account Number is: " + accountNumber);
        return accountNumber;
    }

    @Override
    public Account findByAccountNumber(Long accountNumber) {
        List<Account> accounts = accountDao.getAccountByNumber(accountNumber);

        int listSize = accounts.size();
        if (listSize > 1) {
            throw new NonUniqueResultException("Table Account not consistent");
        } else if (listSize == 1) {
            return accounts.get(0);
        }
        return null;
    }

    @Override
    public Account getClone(Account account) {
        LOGGER.debug("Start cloning the Account: " + account);
        byte[] byteData;
        //read
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(account);
            oos.flush();
            byteData = bos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Exception while reading the account object for cloning");
            throw new RuntimeException(e);
        }
        //write
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteData)) {
            account = (Account) new ObjectInputStream(bais).readObject();
        } catch (Exception e) {
            LOGGER.error("Exception while writing the account object for cloning");
            throw new RuntimeException(e);
        }
        LOGGER.debug("Cloned account object is " + account);
        return account;
    }

    public List<Account> filterByStatus(List<Account> accounts, Status status) {
        return  accounts.stream().filter(account ->
                account.getStatus().equals(status)).collect(Collectors.toList());
    }
}