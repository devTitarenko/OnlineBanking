package com.eisgroup.cb.customer.converters;

import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
@FacesConverter("dateConverter")
public class DateConverter implements Converter {
    private static final Format dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.UK);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return dateFormat.format(value);
    }
}