package com.eisgroup.cb.customer.beans;

import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.model.Status;
import com.eisgroup.cb.service.CustomerService;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class CustomerMainBean implements Serializable {

    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;

    private Customer customer;
    private List<Account> accounts;
    private List<Card> cardList;

    private static final Logger LOGGER = Logger.getLogger(CustomerMainBean.class);
    private static final Format dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);

    @PostConstruct
    public void init() {
        customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customerCustomerApp");
        accounts = (List<Account>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("accountsCustomerApp");
        accounts = accounts.stream().filter(account ->
                account.getStatus().equals(Status.ACTIVE)).collect(Collectors.toList());
        cardList = (List<Card>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cardsCustomerApp");
        cardList = cardList.stream().filter(card ->
                card.getStatus().equals(Status.ACTIVE)).collect(Collectors.toList());
    }

    public void onRowSelect(SelectEvent event) {
        LOGGER.info(event.getObject());
        String url = event.getComponent().getId().equals("customerMainAccountsTable")
                ? "accounts.xhtml?i=0" : "cards.xhtml?i=1";
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ExchangeRates> getExchangeRates() {
        List<ExchangeRates> list = new ArrayList<>();
        list.add(new ExchangeRates("EUR", 1, 28.2500D, 28.6500D, 28.5567D));
        list.add(new ExchangeRates("USD", 1, 26.6500D, 26.9000D, 26.8642D));
        list.add(new ExchangeRates("RUB", 1, 0.4550D, 0.4750D, 0.4746D));
        return list;
    }

    public class ExchangeRates {
        private String currency;
        private Integer quantity;
        private Double purchase;
        private Double sell;
        private Double nbuRate;

        ExchangeRates(String currency, Integer quantity, Double purchase, Double sell, Double nbuRate) {
            this.currency = currency;
            this.quantity = quantity;
            this.purchase = purchase;
            this.sell = sell;
            this.nbuRate = nbuRate;
        }

        public String getCurrency() {
            return currency;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public Double getPurchase() {
            return purchase;
        }

        public Double getSell() {
            return sell;
        }

        public Double getNbuRate() {
            return nbuRate;
        }
    }


    //    Getters and Setters
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public String getCurrentDate() {
        return dateFormat.format(new Date());
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}