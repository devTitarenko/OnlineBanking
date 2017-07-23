package com.eisgroup.cb.validators;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Component
@Scope("request")
public class ValidToValidator implements Validator {

    private ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        UIInput uiInput = (UIInput) component.getAttributes().get("validFrom");
        Object validFromValue = (uiInput.getValue() == null) ? uiInput.getSubmittedValue() : uiInput.getValue();
        LocalDate validFrom = ((Date) validFromValue).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate validTo = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date todayDate = c.getTime();
        LocalDate today = (todayDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (validTo.isBefore(today)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.validTo.prior")));
        }


        else if (validTo.isBefore(validFrom) || validTo.equals(validFrom)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.validTo.outside")));
        }

    }
}
