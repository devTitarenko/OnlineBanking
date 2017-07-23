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
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Component
@Scope("request")
public class CardExpirationDateValidator implements Validator {

    private ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {

        if (value == null) {
            return;
        }

        UIInput uiEffectiveDate = (UIInput) uiComponent.getAttributes().get("effectiveDate");
        Object effectiveDateValue = (uiEffectiveDate.getValue() == null) ? uiEffectiveDate.getSubmittedValue() : uiEffectiveDate.getValue();
        LocalDate effectiveDate = ((Date) effectiveDateValue).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate expirationDate = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Date validTo = (Date) uiComponent.getAttributes().get("accountValidTo");
        LocalDate accountValidTo = validTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (expirationDate.isBefore(effectiveDate) || expirationDate.equals(effectiveDate)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.expirationDate.priorEffective")));
        }

        if (expirationDate.isAfter(accountValidTo)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.expirationDate.afterValidTo")));
        }
    }
}