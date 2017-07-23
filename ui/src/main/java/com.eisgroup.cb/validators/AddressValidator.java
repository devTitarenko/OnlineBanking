package com.eisgroup.cb.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FacesValidator
public class AddressValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        String inputString = String.valueOf(value);
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");

        String componentId = component.getId();
        if (componentId.charAt(componentId.length() - 1) == '2') {
            componentId = componentId.substring(0, componentId.length() - 1);
        }

        Pattern pattern = Pattern.compile("^[\\w\\s~!@#$%^&*()_+-={}\\[\\]:;\"'|,./<>?\n\r\\\\]{1,255}$");
        Matcher m = pattern.matcher(inputString);

        if (inputString.trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error." + componentId + ".mandatory")));
        } else if (!m.matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error." + componentId + ".wrongFormat")));
        }
    }
}