package com.eisgroup.cb.beans;

import com.eisgroup.cb.dto.AccountDTO;
import com.eisgroup.cb.model.Account;
import com.eisgroup.cb.service.AccountService;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class AccountBean implements Serializable {

    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;

    private Mode mode;
    private Account account;
    private Boolean isLimitVisible;
    private AccountDTO accountDto;

    private static final Logger LOGGER = Logger.getLogger(AccountBean.class);

    @PostConstruct
    public void init() {
        mode = (Mode) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mode");
        account = (Account) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("account");
        LOGGER.info(account);
        mapAccToDto();
    }

    private void mapAccToDto() {
        LOGGER.info(account);
        accountDto = new AccountDTO(account);
    }

    public void accountTypeChangeListener() {
        Account.Type type = accountDto.getType();
        LOGGER.info("Account type: " + type);
        setLimitVisible(accountDto.getType().equals(Account.Type.LOAN));
        LOGGER.info("Is Account.Type == LOAN Result: " + getLimitVisible());
        accountDto.setAccountNumber(accountService.generateAccountNumber(type));
    }

    public String preSaveAccount() {
        LOGGER.info("Pre-saving... " + account);
        mapDtoToAcc();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isAccountSaved", true);
        return "AddEditCustomer";
    }

    private void mapDtoToAcc() {
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setType(accountDto.getType());
        account.setValidFrom(accountDto.getValidFrom());
        account.setValidTo(accountDto.getValidTo());
        account.setBalance(accountDto.getBalance());
        account.setLimit(accountDto.getLimit());
        account.setCurrency(accountDto.getCurrency());
    }

    public String backToCustomerForm() {
        if (mode == Mode.ADD) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isAccountSaved", false);
        }
        return "AddEditCustomer";
    }

    //    Getters and Setters
    public Account.Type[] getAccountTypes() {
        return Account.Type.values();
    }

    public Account.Currency[] getAccountCurrency() {
        return Account.Currency.values();
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Boolean getLimitVisible() {
        return isLimitVisible;
    }

    public void setLimitVisible(Boolean limitVisible) {
        isLimitVisible = limitVisible;
    }

    public AccountDTO getAccountDto() {
        return accountDto;
    }

    public void setAccountDto(AccountDTO accountDto) {
        this.accountDto = accountDto;
    }
}