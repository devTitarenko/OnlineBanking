package com.eisgroup.cb.converters;

import com.eisgroup.cb.model.Country;
import com.eisgroup.cb.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Component
@FacesConverter("countryConverter")
public class CountryConverter implements Converter {

    @Autowired
    private CountryService countryService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return countryService.getCountryById(Long.parseLong(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object != null && object.getClass().equals(Country.class)) {
            return ((Country) object).getId().toString();
        } else {
            return null;
        }
    }
}