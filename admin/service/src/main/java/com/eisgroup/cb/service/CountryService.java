package com.eisgroup.cb.service;

import com.eisgroup.cb.model.Country;

import java.util.List;
import java.util.Map;

public interface CountryService {
    List<Country> getAllCountriesList();

    Map<String, Country> getAllCountriesMap();

    Country getCountryByName(String countryName);

    Country getCountryById(long id);
}
