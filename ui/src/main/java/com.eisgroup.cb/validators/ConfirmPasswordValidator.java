package com.eisgroup.cb.validators;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
public class ConfirmPasswordValidator implements Validator {

    private static final Logger LOGGER = Logger.getLogger(ConfirmPasswordValidator.class);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
        if (value == null) {
            return;
        }
        String DefPassword = context.getExternalContext().getRequestParameterMap().get("createEditCustomerForm:accordion:passDef");
        String confirmPassword = (String) value;

            if (!confirmPassword.equals(DefPassword)) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                        resourceBundle.getString("Error.passConf.confirm")));
            }
            if (confirmPassword.length()<8) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                        resourceBundle.getString("Error.passConf.empty")));
        }
        }
    }
