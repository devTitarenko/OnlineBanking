package com.eisgroup.cb.customer.beans;

import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.model.Status;
import com.eisgroup.cb.service.CustomerService;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class CustomerLoginBean implements Serializable {

    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;
    @ManagedProperty(value = "#{authenticationManager}")
    private AuthenticationManager authenticationManager;

    private ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");

    private Customer customer;
    private String showMsg;
    private Long cardOrAccountNumber;
    private Long tin;

    private static final Logger LOGGER = Logger.getLogger(CustomerLoginBean.class);

    @PostConstruct
    public void init() {
        showMsg = "";
        customer = new Customer();
    }

    public String login() {
//        try {
        if (customerService.isCustomerValid(customer)) {
            Authentication request = new UsernamePasswordAuthenticationToken("user", "user");
            Authentication result = authenticationManager.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);

            FacesContext context = FacesContext.getCurrentInstance();
            Customer newCustomer = customerService.getCustomerByMobileNumber(customer.getMobileNum());
            context.getExternalContext().getSessionMap().put("customerCustomerApp", newCustomer);
            context.getExternalContext().getSessionMap().put("accountsCustomerApp", newCustomer.getAccountList());
            List<Card> cardList = new ArrayList<>();
            newCustomer.getAccountList().forEach(account -> cardList.addAll(account.getCards()));
            context.getExternalContext().getSessionMap().put("cardsCustomerApp", cardList);

            return "Main";
        } else {
            showMsg = resourceBundle.getString("error.customer.SignIn");
        }
//        } catch (AuthenticationException e) {
//            showMsg = resourceBundle.getString("error.customer.SignIn");
//        }
        return null;
    }

    public String logout() {
        SecurityContextHolder.clearContext();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        return "Exit";
    }

    public String forgotPasswordGoChangePassword() {
        customer = customerService.findCustomer(cardOrAccountNumber, String.valueOf(tin));
        if (customer != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("customerForgotPassword", customer);
            return "ChangePassword";
        }
        return null;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getShowMsg() {
        return showMsg;
    }

    public void setShowMsg(String showMsg) {
        this.showMsg = showMsg;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Long getTin() {
        return tin;
    }

    public void setTin(Long tin) {
        this.tin = tin;
    }

    public Long getCardOrAccountNumber() {
        return cardOrAccountNumber;
    }

    public void setCardOrAccountNumber(Long cardOrAccountNumber) {
        this.cardOrAccountNumber = cardOrAccountNumber;
    }
}
