package com.eisgroup.cb.validators;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Scope("request")
public class FirstNameValidator implements Validator {


    String inputString;
    boolean buttonPr;

    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        inputString = String.valueOf(value);
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");

        String p = "^([a-zA-Z]+[a-zA-Z\\s\\-]*){0,255}$";
        Pattern pattern = Pattern.compile(p);
        Matcher m = pattern.matcher(inputString);

        if (inputString.trim().isEmpty()) {
            setButtonTrue();
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.firstName.mandatory")));
        } else if ((!m.matches()) && (isButtonPr())) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.firstName.wrongFormat")));
        }
    }

    public boolean isButtonPr() {
        return buttonPr;
    }

    public void setButtonPr(boolean buttonPr) {
        this.buttonPr = buttonPr;
    }

    public void setButtonTrue() {
        this.buttonPr = true;
    }

    public void setButtonFalse() {
        this.buttonPr = false;
    }

}