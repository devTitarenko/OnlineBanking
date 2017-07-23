package com.eisgroup.cb.dao;

import com.eisgroup.cb.model.Country;

public interface CountryDao extends BaseObjectDAO<Country> {
    Country getCountryByName(String countryName);
}
