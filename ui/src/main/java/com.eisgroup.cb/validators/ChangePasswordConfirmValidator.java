package com.eisgroup.cb.validators;

import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.service.CustomerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Component
@Scope("request")
public class ChangePasswordConfirmValidator implements Validator {


    @Autowired
    private CustomerService customerService;

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordConfirmValidator.class);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
        if (value == null) {
            return;
        }
        Customer customer=(Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerForgotPassword");
        Long customerId=customer.getId();
        /*Long customerId = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerId");*/
        String confirmPassword = (String) value;
        UIInput uiInput = (UIInput) component.getAttributes().get("passNew");
        String newPassword = (String)((uiInput.getValue()==null) ? uiInput.getSubmittedValue() : uiInput.getValue());
        if (!confirmPassword.equals(newPassword)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.changePassword.confirmPassword.confirm")));
        }
        LOGGER.info(customerId);
        LOGGER.info(confirmPassword);
        LOGGER.info(customerService);
        //Future functionality
/*        if (customerService.isPasswordDuplicate(customerId, confirmPassword)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.changePassword.confirmPassword.differ")));
        }*/
        if (confirmPassword.equals(customerService.getCustomerById(customerId).getPassword())) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.changePassword.confirmPassword.differ")));
        }

    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
