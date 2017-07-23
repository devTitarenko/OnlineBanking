package com.eisgroup.cb.converters;

import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Component
@FacesConverter("spaceTrimmer")
public class SpaceTrimmer implements Converter{
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return String.valueOf(value).trim();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return String.valueOf(value);
    }
}