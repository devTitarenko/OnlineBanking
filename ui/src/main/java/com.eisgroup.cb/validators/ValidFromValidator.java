package com.eisgroup.cb.validators;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Component
@Scope("request")
public class ValidFromValidator implements Validator {

    private ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate validFrom = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (validFrom.isBefore(today)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.validFrom.outside")));
        }
    }
}
