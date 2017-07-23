package com.eisgroup.cb.validators;

import com.eisgroup.cb.beans.Mode;
import com.eisgroup.cb.model.Account;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesValidator
public class DoubleValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        Double inputDouble;
        String inputString = String.valueOf(value);
        String componentId = component.getId();
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
        Mode mode = (Mode) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mode");

        Pattern pattern = Pattern.compile("/[^0-9]+/");
        Matcher m = pattern.matcher(inputString);

        if (m.matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                   resourceBundle.getString("Error." + component.getId() + ".monetary")));
        }


        try {
            inputDouble = Double.parseDouble(inputString);
        } catch (NumberFormatException e) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error." + component.getId() + ".monetary")));
        }
        if (inputDouble < 0) {
            String exceptionMessage = "Error." + component.getId() + ".negative";
            if (componentId.equals("accountBalance") && mode.equals(Mode.EDIT)) {
                Account.Type type = (Account.Type) ((UIInput) component.getAttributes().get("accType")).getValue();
                Double limit = (Double) ((UIInput) component.getAttributes().get("accLimit")).getValue();
                if (type.equals(Account.Type.LOAN) && Math.abs(inputDouble) <= limit) {
                    return;
                } else {
                    exceptionMessage += "." + type;
                }
            }
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString(exceptionMessage)));
        }
    }
}
