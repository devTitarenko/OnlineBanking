package com.eisgroup.cb.dao;

import com.eisgroup.cb.model.Account;

import java.util.List;

public interface AccountDao extends BaseObjectDAO<Account>, LastNumberGetter {
    List<Account> getAccountByNumber(Long accountNumber);
}