package com.eisgroup.cb.converters;

import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.Customer;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Component
@FacesConverter("accountConverter")
public class AccountConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Customer customer = (Customer) component.getAttributes().get("customer");
        for (Account account : customer.getAccountList()) {
            if (account.getAccountNumber().equals(Long.parseLong(value))) {
                return account;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object != null && object.getClass().equals(Account.class)) {
            return ((Account) object).getAccountNumber().toString();
        } else {
            return null;
        }
    }
}