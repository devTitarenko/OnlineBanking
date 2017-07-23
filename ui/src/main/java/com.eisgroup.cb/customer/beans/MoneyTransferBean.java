package com.eisgroup.cb.customer.beans;

import com.eisgroup.cb.model.*;
import com.eisgroup.cb.service.AccountService;
import com.eisgroup.cb.service.CardService;
import com.eisgroup.cb.service.CustomerService;
import com.eisgroup.cb.service.TransferRecordService;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.validator.ValidatorException;
import javax.persistence.OptimisticLockException;
import java.io.Serializable;
import java.util.*;

@ManagedBean
@ViewScoped
public class MoneyTransferBean implements Serializable {

    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;

    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;

    @ManagedProperty(value = "#{cardService}")
    private CardService cardService;

    @ManagedProperty(value = "#{transferRecordService}")
    private TransferRecordService transferRecordService;

    private Customer customer;
    private List<Card> cards;
    private List<Account> accounts;
    private List<SelectItem> listForDropDown;
    private PaymentMethod transferFrom;
    private PaymentMethod transferTo;
    private Double amount;
    private Long inputtedCardNumber;
    private boolean transferWithinBank;
    private boolean hasAccountOrCard = true;
    //values received from selectOneMenu

    private static final Logger LOGGER = Logger.getLogger(MoneyTransferBean.class);

    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        customer = (Customer) context.getSessionMap().get("customerCustomerApp");
        accounts = (List<Account>) context.getSessionMap().get("accountsCustomerApp");
        cards = (List<Card>) context.getSessionMap().get("cardsCustomerApp");

        //Should filterByStatus be in Service
        cards = cardService.filterByStatus(cards, Status.ACTIVE);
        accounts = accountService.filterByStatus(accounts, Status.ACTIVE);
        if (cards.isEmpty() && accounts.isEmpty()) {
            hasAccountOrCard = false;
        }
        resetForm();
    }

    private void resetForm() {
        transferFrom = null;
        transferTo = null;
        amount = null;
        inputtedCardNumber = null;
        listForDropDown = new ArrayList<>();
        groupByClassName("Cards:", cards);
        groupByClassName("Accounts:", accounts);
    }

    public void transferMoney() {
        if (transferWithinBank) {
            transferTo = cardService.findByCardNumber(inputtedCardNumber);
            transferMoney(transferFrom.getPaymentAccount(), transferTo.getPaymentAccount());
        } else {
            transferMoney(transferFrom.getPaymentAccount(), transferTo.getPaymentAccount());
        }
        init();
    }

    /*@Transactional*/
    public void transferMoney(Account accountFrom, Account accountTo) {
        try {
            LOGGER.info("Before: Account From balance " + accountFrom.getBalance() + " account to balance " + accountTo.getBalance());
            Double balanceFromBeforeTransfer = accountFrom.getBalance();
            Double balanceToBeforeTransfer = accountTo.getBalance();
            withdraw(accountFrom, amount);
            deposit(accountTo, amount);
            accountService.save(accountFrom);
            accountService.save(accountTo);
            generateTransferRecord(accountFrom, accountTo, balanceFromBeforeTransfer, balanceToBeforeTransfer);
            openDialog();
        } catch (OptimisticLockException exception) {
            init();
            ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("money.transfer.concurrent.change.conflict"));
            FacesContext.getCurrentInstance().addMessage(null, msg);
            exception.printStackTrace();

            LOGGER.info("After: Account From balance " + accountFrom.getBalance() + " account to balance " + accountTo.getBalance());
        }
    }

    //should be in Account class?

    private void withdraw(Account account, double delta) {
        account.setBalance((account.getBalance() - delta));
    }

    //should be in Account class?
    private void deposit(Account account, double delta) {
        account.setBalance((account.getBalance() + delta));
    }

    private void generateTransferRecord(Account accountFrom, Account accountTo, Double balanceFromBefore,
                                        Double balanceToBefore) {
        TransferRecord transferRecordFrom = prepopulateRecord();
        transferRecordFrom.setCreditAccountNumber(accountFrom.getAccountNumber());
        transferRecordFrom.setDebitAccountNumber(accountTo.getAccountNumber());
        transferRecordFrom.setTransactionCurrency(accountFrom.getCurrency());
        transferRecordFrom.setStartingBalance(balanceFromBefore);
        transferRecordFrom.setEndingBalance(accountFrom.getBalance());
        transferRecordFrom.setEntryType(TransferRecord.EntryType.CREDIT);

        TransferRecord transferRecordTo = prepopulateRecord();
        transferRecordTo.setCreditAccountNumber(accountFrom.getAccountNumber());
        transferRecordTo.setDebitAccountNumber(accountTo.getAccountNumber());
        transferRecordTo.setTransactionCurrency(accountFrom.getCurrency());
        transferRecordTo.setStartingBalance(balanceToBefore);
        transferRecordTo.setEndingBalance(accountTo.getBalance());
        transferRecordTo.setEntryType(TransferRecord.EntryType.DEBIT);

        if (PaymentMethod.PaymentType.CARD.equals(transferFrom.getPaymentsType())) {
            Card selectedCardFrom = (Card) transferFrom;
            transferRecordFrom.setCardFromNumber(selectedCardFrom.getCardNumber());
            transferRecordTo.setCardFromNumber(selectedCardFrom.getCardNumber());
        }

        if (PaymentMethod.PaymentType.CARD.equals(transferTo.getPaymentsType())) {
            Card selectedCardTo = (Card) transferTo;
            transferRecordFrom.setCardToNumber(selectedCardTo.getCardNumber());
            transferRecordTo.setCardToNumber(selectedCardTo.getCardNumber());
        }

        transferRecordService.save(transferRecordFrom);
        transferRecordService.save(transferRecordTo);
    }

    private TransferRecord prepopulateRecord() {
        TransferRecord transferRecord = new TransferRecord();
        transferRecord.setTransactionDateAndTime(new Date());
        transferRecord.setTransactionType(TransferRecord.TransactionType.MONEY_TRANSFER);
        if (transferWithinBank) {
            transferRecord.setTransactionSubtype(TransferRecord.TransactionSubtype.WITHIN_BANK);
        } else {
            transferRecord.setTransactionSubtype(TransferRecord.TransactionSubtype.BETWEEN_ACCOUNTS);
        }
        transferRecord.setTransactionAmount(amount);
        transferRecord.setTransactionLocation(TransferRecord.Location.ONLINE_BANKING);
        transferRecord.setPerformerId(customer.getId());
        return transferRecord;
    }

    private void openDialog() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Customer customer = customerService.getCustomerById(this.customer.getId());
        List<Card> cards = new ArrayList<>();
        customer.getAccountList().forEach(account -> cards.addAll(account.getCards()));

        context.getSessionMap().put("customerCustomerApp", customer);
        context.getSessionMap().put("accountsCustomerApp", customer.getAccountList());
        context.getSessionMap().put("cardsCustomerApp", cards);

        RequestContext.getCurrentInstance().execute("PF('transferDialog').show();");
    }

    private void groupByClassName(String className, List<? extends PaymentMethod> values) {
        SelectItemGroup group = new SelectItemGroup(className);
        List<SelectItem> itemList = new ArrayList<>();
        for (PaymentMethod s : values) {
            itemList.add(new SelectItem(s, s.getLabel()));
        }
        SelectItem[] items = new SelectItem[itemList.size()];
        itemList.toArray(items);
        group.setSelectItems(items);
        listForDropDown.add(group);
    }

    public void validateAccounts(FacesContext context, UIComponent component, Object value) {
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
        if (value == null) {
            return;
        }
        UIInput uiInput = (UIInput) component.getAttributes().get("transferFrom");
        PaymentMethod transferFrom = (PaymentMethod) uiInput.getValue();
        PaymentMethod transferTo = (PaymentMethod) value;
        if (transferFrom == null) {
            return;
        }
        Account accountFrom = transferFrom.getPaymentAccount();
        Account accountTo = transferTo.getPaymentAccount();
        if (accountFrom.equals(accountTo)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("money.transfer.same.account.for.transfer")));
        } else if (accountFrom.getCurrency() != accountTo.getCurrency()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("money.transfer.different.currency.types")));
        }
    }

    public void validateOtherCard(FacesContext context, UIComponent component, Object value) {
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
        if (value == null) {
            return;
        }
        String s = (String) value;
        if (!s.matches("^[\\d]{16}$")) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("money.transfer.transfer.within.bank.cardNumber.format")));
        }
        Long cardNumber = Long.parseLong(s);
        Card card = cardService.findByCardNumber(cardNumber);
        UIInput uiInput = (UIInput) component.getAttributes().get("transferFrom");
        PaymentMethod transferFrom = (PaymentMethod) uiInput.getValue();
        if (transferFrom == null) {
            return;
        }
        if (card == null || card.getStatus().equals(Status.INACTIVE) || card.getStatus().equals(Status.BLOCKED)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("money.transfer.card.number.wrong.status")));
        }
        Customer customer = customerService.findByAccountNumber(card.getAccount().getAccountNumber());
        if (customer.equals(this.customer)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("money.transfer.card.number.same.owner")));
        }
        Account accountFrom = transferFrom.getPaymentAccount();
        Account accountTo = card.getAccount();

        if (accountFrom.getCurrency() != accountTo.getCurrency()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("money.transfer.different.currency.types")));
        }

    }

    public void validateAmount(FacesContext context, UIComponent component, Object value) {
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");
        if (value == null) {
            return;
        }
        Double amount = (Double) value;
        if (!((amount.toString().matches("^(?:\\d*\\.)?\\d+$")) && (amount > 0))) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("money.transfer.transfer.amount.positive.monetary")));
        }
        UIInput uiInput = (UIInput) component.findComponent("transferFrom");
        PaymentMethod accountFrom = uiInput.getLocalValue() == null ? null
                : (PaymentMethod) uiInput.getLocalValue();
        if (accountFrom == null) {
            return;
        }

        Double balance = accountFrom.getPaymentAccount().getBalance();
        Double limit = accountFrom.getPaymentAccount().getLimit();
        if (!(balance + limit >= amount)) {
            init();
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", resourceBundle.getString("money.transfer.amount.exceeds")));
        }

    }

    //Getters and setters

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    public void setTransferRecordService(TransferRecordService transferRecordService) {
        this.transferRecordService = transferRecordService;
    }

    public CardService getCardService() {
        return cardService;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isTransferWithinBank() {
        return transferWithinBank;
    }

    public void setTransferWithinBank(boolean transferWithinBank) {
        this.transferWithinBank = transferWithinBank;
    }

    public String getInputtedCardNumber() {
        return inputtedCardNumber == null ? "" : inputtedCardNumber.toString();
    }

    public void setInputtedCardNumber(String inputtedCardNumber) {
        if (!inputtedCardNumber.isEmpty()) {
            this.inputtedCardNumber = Long.valueOf(inputtedCardNumber);
        }
    }

    public List<SelectItem> getListForDropDown() {
        return listForDropDown;
    }

    public void setListForDropDown(List<SelectItem> listForDropDown) {
        this.listForDropDown = listForDropDown;
    }

    public PaymentMethod getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(PaymentMethod transferFrom) {
        this.transferFrom = transferFrom;
    }

    public PaymentMethod getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(PaymentMethod transferTo) {
        this.transferTo = transferTo;
    }

    public boolean isHasAccountOrCard() {
        return hasAccountOrCard;
    }

    public void setHasAccountOrCard(boolean hasAccountOrCard) {
        this.hasAccountOrCard = hasAccountOrCard;
    }
}
