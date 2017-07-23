package com.eisgroup.cb.dao.impl;

import com.eisgroup.cb.dao.CountryDao;
import com.eisgroup.cb.model.Country;
import org.springframework.stereotype.Repository;

@Repository
public class CountryDaoImpl extends BaseObjectDAOImpl<Country> implements CountryDao {

    CountryDaoImpl() {
        super(Country.class);
    }

    @Override
    public Country getCountryByName(String countryName) {
        return entityManager.createQuery(
                "SELECT country FROM Country country WHERE country.country = :name", Country.class)
                .setParameter("name", countryName).getSingleResult();
    }
}