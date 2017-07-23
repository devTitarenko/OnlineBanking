package com.eisgroup.cb.service;

import com.eisgroup.cb.dao.LastNumberGetter;

import javax.persistence.NoResultException;

public abstract class AbstractNumberGenerator {
    private Long number;

    public AbstractNumberGenerator() {
    }

    public AbstractNumberGenerator(LastNumberGetter lastNumberGetter, int digitsNumber) {
        try {
            number = lastNumberGetter.getLastNumber() % (long) Math.pow(10, digitsNumber);
        } catch (NoResultException e) {
            number = 0L;
        }
    }

    protected Long getNext() {
        return ++number;
    }
}