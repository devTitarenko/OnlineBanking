package com.eisgroup.cb.beans;

import com.eisgroup.cb.exceptions.PasswordDuplicateException;
import com.eisgroup.cb.model.*;
import com.eisgroup.cb.service.CardService;
import com.eisgroup.cb.service.CustomerService;
import org.apache.log4j.Logger;
import org.primefaces.event.TabChangeEvent;
import org.springframework.beans.BeanUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class CustomerBean implements Serializable {

    @ManagedProperty(value = "#{customerService}")
    private CustomerService customerService;
    @ManagedProperty(value = "#{countryBean}")
    private CountryBean countryBean;
    @ManagedProperty(value = "#{cardService}")
    private CardService cardService;

    private Long customerId;
    private Customer customer;
    private List<Country> allCountriesList;
    private boolean sameAddress;
    private boolean tinDuplicate;
    private boolean mobileDuplicate;
    private boolean passwordDuplicate;
    private boolean addCardEnabled;
    private String firstNameField;
    private Mode mode;
    private Account accountForDeactivation;
    private Card cardForDeactivation;
    private String activeAccordionTab;
    private List<Card> cards;
    private final String CUSTOMER_ACCRORDION_TAB_GENERAL = "0";
    private final String CUSTOMER_ACCRORDION_TAB_ACCOUNTS = "3";
    private final String CUSTOMER_ACCRORDION_TAB_CARDS = "4";

    private static final Logger LOGGER = Logger.getLogger(CustomerBean.class);

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        customer = (Customer) context.getExternalContext().getSessionMap().get("customer");
        if (customer == null) {
            LOGGER.info("Initialization of new Customer...");
            setMode(Mode.ADD);
            sameAddress = true;
            customer = new Customer();
            customer.setAccountList(new ArrayList<>());
            Address regAddress = new Address();
            regAddress.setCountry(countryBean.getDefaultCountry());
            customer.setRegAddress(regAddress);
            activeAccordionTab = CUSTOMER_ACCRORDION_TAB_GENERAL;
            cards = Collections.emptyList();
            checkAddCardButton();

            LOGGER.info("AddCardEnabled: " + addCardEnabled);
        } else {
            activeAccordionTab = (String) context.getExternalContext().getSessionMap().get("activeAccordionTab");
            Boolean isAccountSaved = (Boolean) context.getExternalContext().getSessionMap().get("isAccountSaved");
            Mode externalViewMode = (Mode) context.getExternalContext().getSessionMap().get("mode");

            LOGGER.info(isAccountSaved);
            if (isAccountSaved != null && !isAccountSaved && Mode.ADD.equals(externalViewMode)) {
                customer.getAccountList().remove(context.getExternalContext().getSessionMap().get("account"));
            }
            updateCustomer();
        }
        allCountriesList = countryBean.getAllCountriesList();
        LOGGER.info("Initialized: " + customer);
    }

    public String saveCustomer() {
        if (checkDuplication()) {
            LOGGER.info("SAVING... - " + customer);
            if (sameAddress) {
                Address mailAddress = new Address();
                BeanUtils.copyProperties(customer.getRegAddress(), mailAddress, "id");
                customer.setMailAddress(mailAddress);
            }

            customer.getAccountList().forEach(account -> {
                List<Card> cards = account.getCards();
                if (cards != null) {
                    account.getCards().stream().filter(Objects::nonNull)
                            .forEach(card -> card.setAccount(account));
                }
            });

            try {
                customerService.save(customer);
                return "SearchCustomer";
            } catch (PasswordDuplicateException e) {
                passwordDuplicate = true;
                LOGGER.error(e.getMessage());
            }
        }
        return null;
    }

    private void updateCustomer() {
        LOGGER.info("UPDATING... - " + customer);
        if (customer.getId() != null) {
            mode = Mode.EDIT;
        } else {
            mode = Mode.ADD;
        }


        if (cards == null) {
            cards = new ArrayList<>();
            customer.getAccountList().stream().filter(account -> account.getCards() != null)
                    .forEach(account -> cards.addAll(account.getCards()));
        } else {
            cards = customer.getAccountList().stream()
                    .flatMap(account -> account.getCards().stream())
                    .collect(Collectors.toList());
        }

        sameAddress = (customer.getMailAddress() == null) ||
                customer.getRegAddress().equals(customer.getMailAddress());
        checkAddCardButton();
    }

    public void updateCustomerById() {
        if (customerId == null) return;
        customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Non-existing user id.");
        }
        updateCustomer();
    }


    private boolean checkDuplication() {
        LOGGER.info("Checking duplication for " + customer);
        tinDuplicate = customerService.isTinExists(customer);
        mobileDuplicate = customerService.isMobileNumExists(customer);
        LOGGER.info("Duplicated? - " + (tinDuplicate || mobileDuplicate));
        return !tinDuplicate && !mobileDuplicate;
    }



    private String goToAccountForm(Mode mode, Account account) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("mode", mode);
        context.getExternalContext().getSessionMap().put("account", account);
        context.getExternalContext().getSessionMap().put("customer", customer);
        context.getExternalContext().getSessionMap().put("activeAccordionTab", CUSTOMER_ACCRORDION_TAB_ACCOUNTS);

        return "AddAccount";
    }

    public String addAccount() {
        Account account = new Account();
        account.setCurrency(Account.Currency.UAH);
        account.setBalance(0d);
        account.setLimit(0d);
        account.setValidFrom(new Date());
        customer.getAccountList().add(account);
        return goToAccountForm(Mode.ADD, account);
    }

    public String viewAccount(Account account) {
        return goToAccountForm(Mode.VIEW, account);
    }

    public String editAccount(Account account) {
        return goToAccountForm(Mode.EDIT, account);
    }

    public String backToSearch() {
        customer = null;
        goToAccountForm(null, null);
        return "SearchCustomer";
    }

    public void deactivation() {
        accountForDeactivation.setValidTo(new Date());
        if (accountForDeactivation.getCards() != null) {
            accountForDeactivation.getCards().stream().filter(card -> card.getStatus().equals(Status.ACTIVE))
                    .collect(Collectors.toList()).forEach(card -> card.setExpirationDate(new Date()));
        }
        checkAddCardButton();
    }

    public void cardDeactivation() {
        cardForDeactivation.setExpirationDate(new Date());
        checkAddCardButton();
    }

    private void checkAddCardButton() {
        List<Account> accounts = customer.getAccountList();
        addCardEnabled = (accounts != null &&
                (accounts.stream().filter(account -> account.getStatus() == Status.ACTIVE)
                        .map(Account::getType)
                        .anyMatch(type -> type == Account.Type.LOAN || type == Account.Type.PAYMENT)));
    }

    private String goToCardForm(Mode mode, Card cardForView) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("mode", mode);
        context.getExternalContext().getSessionMap().put("customer", customer);
        context.getExternalContext().getSessionMap().put("cardForView", cardForView);
        context.getExternalContext().getSessionMap().put("activeAccordionTab", CUSTOMER_ACCRORDION_TAB_CARDS);
        return "AddCard";
    }

    public String addCard() {
        Card card = new Card();
        LOGGER.info("Initialization of new Card...");

        card.setCvv2(generateCVV2());
        card.setCardHolder(customer.getFirstName() + " " + customer.getLastName());

        return goToCardForm(Mode.ADD, card);
    }

    public String viewCard(Card card) {
        return goToCardForm(Mode.VIEW, card);
    }

    public String editCard(Card card) {

        return goToCardForm(Mode.EDIT, card);
    }

    public void onTabChange(TabChangeEvent event) {
        LOGGER.info("tab id = " + event.getTab().getId());
    }

    public void lastNameChangeListener() {
        if (customer.getCodeword() == null) {
            customer.setCodeword(customer.getLastName());
        }
    }

    private String generateCVV2() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(1000);
        return String.format("%03d", randomInt);
    }

/*    public boolean isAddCardEnabled() {
        List<Account> accounts = customer.getAccountList();
        if (accounts != null &&
                (accounts.stream().anyMatch(account -> account.getType() == Account.Type.LOAN)
                        || accounts.stream().anyMatch(account -> account.getType() == Account.Type.PAYMENT))) {
            return true;
        }
        return false;
    }*/


    //    Getters and Setters
    public Gender[] getGenders() {
        return Gender.values();
    }

    public MaritalStatus[] getMaritalStatuses() {
        return MaritalStatus.values();
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setCountryBean(CountryBean countryBean) {
        this.countryBean = countryBean;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Country> getAllCountriesList() {
        return allCountriesList;
    }

    public void setAllCountriesList(List<Country> allCountriesList) {
        this.allCountriesList = allCountriesList;
    }

    public boolean isSameAddress() {
        return sameAddress;
    }

    public void setSameAddress(boolean sameAddress) {
        if (sameAddress) {
            Address mailAddress = null;
        } else {
            Address mailAddress = new Address();
            mailAddress.setCountry(countryBean.getDefaultCountry());
            customer.setMailAddress(mailAddress);
        }
        this.sameAddress = sameAddress;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean isTinDuplicate() {
        return tinDuplicate;
    }

    public void setTinDuplicate(boolean tinDuplicate) {
        this.tinDuplicate = tinDuplicate;
    }

    public boolean isMobileDuplicate() {
        return mobileDuplicate;
    }

    public void setMobileDuplicate(boolean mobileDuplicate) {
        this.mobileDuplicate = mobileDuplicate;
    }

    public boolean isPasswordDuplicate() {
        return passwordDuplicate;
    }

    public void setPasswordDuplicate(boolean passwordDuplicate) {
        this.passwordDuplicate = passwordDuplicate;
    }

    public Account getAccountForDeactivation() {
        return accountForDeactivation;
    }

    public void setAccountForDeactivation(Account accountForDeactivation) {
        this.accountForDeactivation = accountForDeactivation;
    }

    public String getActiveAccordionTab() {
        return activeAccordionTab;
    }

    public void setActiveAccordionTab(String activeAccordionTab) {
        this.activeAccordionTab = activeAccordionTab;
    }

    public CardService getCardService() {
        return cardService;
    }

    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Card getCardForDeactivation() {
        return cardForDeactivation;
    }

    public void setCardForDeactivation(Card cardForDeactivation) {
        this.cardForDeactivation = cardForDeactivation;
    }

    public boolean isAddCardEnabled() {
        return addCardEnabled;
    }

    public void setAddCardEnabled(boolean addCardEnabled) {
        this.addCardEnabled = addCardEnabled;
    }

    public String getFirstNameField() {
        return firstNameField;
    }

    public void setFirstNameField(String firstNameField) {
        this.firstNameField = firstNameField;
    }
}

