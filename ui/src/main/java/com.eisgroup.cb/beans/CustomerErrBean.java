package com.eisgroup.cb.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class CustomerErrBean {

    private String firstName;

    private boolean buttonPressed = false;

    public void validate(FacesContext context, UIComponent component, Object value)  {
        if (value == null) {
            return;}
            firstName = (String) value;
        }

    public void buttonPressedTrue(){
      buttonPressed = true;}

    public void buttonPressedFalse(){

       buttonPressed = false;}

    public boolean getButtonPressed() {
        if (firstName.isEmpty()) buttonPressed = true;
        return buttonPressed;}

    public void setButtonPressed(boolean buttonPressed) {
        this.buttonPressed = buttonPressed;
    }

    public String getFirstName() {return firstName;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    /*public boolean getButtonPressed() {
        return ButtonPressed;
    }

    public void setButtonPressed(boolean buttonPressed) {
        isButtonPressed = buttonPressed;
    }*/

   /*  public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        String inputString = String.valueOf(value);
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");

        String p = "^([a-zA-Z]+[a-zA-Z\\s\\-]*){0,255}$";
        Pattern pattern = Pattern.compile(p);
        Matcher m = pattern.matcher(inputString);

        if (inputString.trim().isEmpty()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.firstName.mandatory")));
        } else if ((!m.matches())&&(!inputString.trim().isEmpty())) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.firstName.wrongFormat")));
        }
    } */
}