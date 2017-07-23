package com.eisgroup.cb.customer.beans;

import com.eisgroup.cb.exceptions.PasswordDuplicateException;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.service.CustomerService;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@ManagedBean
@ViewScoped
public class ChangePasswordBean implements Serializable {

    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;

    private String password;
    private Customer customer;

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordBean.class);

    @PostConstruct
    public void init() {
        customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerForgotPassword");
    }

    public String savePassword() throws PasswordDuplicateException {
        customer.setPassword(password);
        try {
            customerService.save(customer);
            return "Exit";
        }catch (PasswordDuplicateException ex){
            ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
            ex.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("Error.changePassword.confirmPassword.differ.from.previous"));
            FacesContext.getCurrentInstance().addMessage("changePasswordForm:newPassword", msg);
        }
        return null;
    }


    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

