package com.eisgroup.cb.beans;

import com.eisgroup.cb.model.Country;
import com.eisgroup.cb.service.CountryService;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import java.util.List;

@ManagedBean
@ApplicationScoped
public class CountryBean {

    @ManagedProperty(value = "#{countryService}")
    private CountryService countryService;

    private List<Country> allCountriesList;
    private Country defaultCountry;

    private static final Logger LOGGER = Logger.getLogger(CustomerBean.class);

    @PostConstruct
    public void init() {
        LOGGER.info("CountryBean initialization...");
        allCountriesList = countryService.getAllCountriesList();
        defaultCountry = countryService.getCountryByName("Ukraine");
    }

    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }

    public List<Country> getAllCountriesList() {
        return allCountriesList;
    }

    public Country getDefaultCountry() {
        return defaultCountry;
    }
}