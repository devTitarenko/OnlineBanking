package com.eisgroup.cb.validators;

import org.apache.log4j.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@FacesValidator
public class ChangePasswordNewValidator implements Validator {

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordNewValidator.class);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
        if (value == null) {
            return;
        }
        String newPassword = (String) value;
        if (newPassword.contains(" ")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.changePassword.newPassword.spaces")));
        }
        if (!newPassword.matches(".*[a-zA-Z]+.*")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.changePassword.newPassword.latin")));
        }
        if (!newPassword.matches(".*\\d+.*")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.changePassword.newPassword.digits")));
        }
        if (newPassword.length() < 8) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.changePassword.newPassword.length")));
        }

    }
}
