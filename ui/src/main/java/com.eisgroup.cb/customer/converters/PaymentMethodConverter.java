package com.eisgroup.cb.customer.converters;

import com.eisgroup.cb.customer.beans.MoneyTransferBean;
import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.model.PaymentMethod;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("paymentMethodConverter")
public class PaymentMethodConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        Customer customer = (Customer) facesContext.getExternalContext().getSessionMap().get("customerCustomerApp");
        FacesContext context = FacesContext.getCurrentInstance();
        MoneyTransferBean bean = context.getApplication().evaluateExpressionGet(context, "#{moneyTransferBean}", MoneyTransferBean.class);

        if (s == null || s.isEmpty()) {
            return null;
        }

        PaymentMethod method;

        if (s.length() == 16) {
            method = bean.getCardService().findByCardNumber(Long.parseLong(s));
            return method;
        } else if (s.length() == 14) {
            for (Account account : customer.getAccountList()) {
                if (account.getAccountNumber().equals(Long.parseLong(s))) {
                    method = account;
                    return method;
                }
            }
        }
        return s;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        String num = null;
        if (o instanceof Card) {
            Card card = (Card) o;
            num = card.getCardNumber().toString();
        } else if (o instanceof Account) {
            Account account = (Account) o;
            num = account.getAccountNumber().toString();

        }
        return num;
    }
}
