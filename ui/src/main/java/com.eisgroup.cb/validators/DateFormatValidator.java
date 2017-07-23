package com.eisgroup.cb.validators;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Component
@Scope("request")
public class DateFormatValidator implements Validator {

    private ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Date date = (Date) value;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = dateFormat.format(date);
        if (!strDate.matches("(0[1-9]|1[0-2])\\/(0[1-9]|1\\d|2\\d|3[01])\\/(18|19|20)\\d{2}")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.dateBirth.wrongFormat")));
        }
    }
}
