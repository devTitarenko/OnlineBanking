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
public class DateOfBirthValidator implements Validator {

    private ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
    private static final int OLD = 120;
    private static final int YOUNG = 18;

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate dateOfBirth = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (!dateOfBirth.isAfter(today.minusYears(OLD).minusDays(2)) || !dateOfBirth.isBefore(today.minusYears(YOUNG).plusDays(1))) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.dateBirth.outside")));
        }
    }
}