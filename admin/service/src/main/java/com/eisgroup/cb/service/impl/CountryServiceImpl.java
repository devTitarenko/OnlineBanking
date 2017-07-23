package com.eisgroup.cb.service.impl;

import com.eisgroup.cb.dao.CountryDao;
import com.eisgroup.cb.model.Country;
import com.eisgroup.cb.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service("countryService")
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryDao countryDao;

    @Override
    public List<Country> getAllCountriesList() {
        return countryDao.getAll();
    }

    @Override
    public Map<String, Country> getAllCountriesMap() {
        Map<String, Country> countryMap = new TreeMap<>();
        for (Country country : getAllCountriesList()) {
            countryMap.put(country.getCountry(), country);
        }
        return countryMap;
    }

    @Override
    public Country getCountryByName(String countryName) {
        return countryDao.getCountryByName(countryName);
    }

    @Override
    public Country getCountryById(long id) {
        return countryDao.find(id);
    }
}