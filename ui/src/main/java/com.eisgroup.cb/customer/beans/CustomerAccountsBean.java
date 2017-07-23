package com.eisgroup.cb.customer.beans;

import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.model.Customer;
import com.eisgroup.cb.model.Status;
import com.eisgroup.cb.service.AccountService;
import com.eisgroup.cb.service.CustomerService;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class CustomerAccountsBean implements Serializable {

    private Account account;
    private Account selectedAccount;
    private List<Account> accountList;
    private boolean rowSelect;
    private boolean hasActiveAccount = false;

    private static final Logger LOGGER = Logger.getLogger(CustomerAccountsBean.class);

    @PostConstruct
    public void init() {
        accountList = (List<Account>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("accountsCustomerApp");
        accountList = accountList.stream().filter(account -> account.getStatus().equals(Status.ACTIVE)).collect(Collectors.toList());
        hasActiveAccount = !accountList.isEmpty();
    }

    public void onRowSelect(SelectEvent event) {
        selectedAccount = (Account) event.getObject();
        LOGGER.info(selectedAccount);
        rowSelect = true;
    }

    public void onRowUnselect(UnselectEvent event) {
        selectedAccount = null;
        rowSelect = false;
    }


    //    Getters and Setters
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public boolean isRowSelect() {
        return rowSelect;
    }

    public void setRowSelect(boolean rowSelect) {
        this.rowSelect = rowSelect;
    }

    public boolean isHasActiveAccount() {
        return hasActiveAccount;
    }

    public void setHasActiveAccount(boolean hasActiveAccount) {
        this.hasActiveAccount = hasActiveAccount;
    }
}