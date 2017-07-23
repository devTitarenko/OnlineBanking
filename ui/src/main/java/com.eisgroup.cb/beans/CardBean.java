package com.eisgroup.cb.beans;

import com.eisgroup.cb.dto.AccountDTO;
import com.eisgroup.cb.dto.CardDTO;
import com.eisgroup.cb.model.*;
import com.eisgroup.cb.service.AccountService;
import com.eisgroup.cb.service.CardService;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.eisgroup.cb.utils.DateUtils.startOfTheDay;

@ManagedBean
@ViewScoped
public class CardBean implements Serializable {
    public static final int THREE_YEARS = 3;

    @ManagedProperty(value = "#{cardService}")
    private CardService cardService;

    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;

    private Customer customer;
    private Card card;
    private List<Account> accounts;
    private Mode mode;
    private Account account;

    private CardDTO cardDTO;
    private AccountDTO accountDto;

    private static final Logger LOGGER = Logger.getLogger(CardBean.class);

    @PostConstruct
    public void init() {
        mode = (Mode) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("mode");
        customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("customer");
        card = (Card) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cardForView");

        if (card.getAccount() != null) {
            account = card.getAccount();
        }

        mapCardToDTO();
        accounts = (customer == null) ? Collections.emptyList() : customer.getAccountList()
                .stream()
                .filter(account1 -> account1.getStatus() == Status.ACTIVE && (account1.getType() == Account.Type.LOAN
                        || account1.getType() == Account.Type.PAYMENT)).collect(Collectors.toList());
    }

    private void mapCardToDTO() {
        cardDTO = new CardDTO(card);
    }

    public CardType[] getCardTypes() {
        return CardType.values();
    }

    public void cardAccountNumberListener() {
        account = getAccount();

        Date today = startOfTheDay(new Date());
        Date effectiveDate = startOfTheDay((today.after(account.getValidFrom())) ? today : account.getValidFrom());
        cardDTO.setEffectiveDate(effectiveDate);

        Calendar c = Calendar.getInstance();
        c.setTime(effectiveDate);
        c.add(Calendar.YEAR, THREE_YEARS);
        Date expirationDate = startOfTheDay((c.getTime().after(account.getValidTo())) ? account.getValidTo() : c.getTime());
        cardDTO.setExpirationDate(expirationDate);
    }

    public String backToCustomerForm() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("customer", customer);
        return "AddEditCustomer";
    }

    private void maoDTOToCard() {
        card.setCardType(cardDTO.getCardType());
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCardHolder(cardDTO.getCardHolder());
        card.setEffectiveDate(cardDTO.getEffectiveDate());
        card.setExpirationDate(cardDTO.getExpirationDate());
        card.setCvv2(cardDTO.getCvv2());
        card.setAccount(account);

        LOGGER.info("Mapping dto to card " + card + cardDTO);
    }

    public String preSaveCard() {
        maoDTOToCard();
        LOGGER.info("Pre-saving... " + card);
        int accountIndex = customer.getAccountList().indexOf(account);

        if (card.getId() != null) {
            int cardIndex = customer.getAccountList().get(accountIndex).getCards().indexOf(card);
            customer.getAccountList().get(accountIndex).getCards().set(cardIndex, card);
            LOGGER.info("Saving card " + customer.getAccountList().get(accountIndex).getCards().get(cardIndex));
        } else {
            //Pre saved account, have to set cards list
            if (customer.getAccountList().get(accountIndex).getCards() == null) {
                List<Card> oneCardList = new ArrayList<>();
                oneCardList.add(card);
                customer.getAccountList().get(accountIndex).setCards(oneCardList);
                LOGGER.info("Saving card in a new card list " + oneCardList.contains(card));
            } else {
                if (customer.getAccountList().get(accountIndex).getCards().contains(card)) {
                    int cardIndex = customer.getAccountList().get(accountIndex).getCards().indexOf(card);
                    customer.getAccountList().get(accountIndex).getCards().set(cardIndex, card);
                    LOGGER.info("Saving existing, but not saved card " + cardIndex);
                } else {
                    customer.getAccountList().get(accountIndex).getCards().add(card);
                }

               /* ArrayList<Card> cards = new ArrayList<>(customer.getAccountList().get(accountIndex).getCards());
                cards.add(card);
                customer.getAccountList().get(accountIndex).getCards().forEach(card1 -> LOGGER.info(card1.toString()));*/
            }
        }
        return "AddEditCustomer";
    }




   /* private String generateCVV2() {
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(1000);
        return String.format("%03d", randomInt);
    }*/

    public void checkLength(FacesContext context, UIComponent component, Object value) {
        LOGGER.info("validating card holder name: ");
        String card1 = (String) value;
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("messages/messages_en");

        if (card1.length() > 511) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",
                    resourceBundle.getString("Error.cardHolder.wrongFormat")));
        }
    }

    public void cardTypeChangeListener() {
        LOGGER.info(cardDTO.getCardType());
        cardDTO.setCardNumber(cardService.generateCardNumber(cardDTO.getCardType()));
    }

    public CardService getCardService() {
        return cardService;
    }

    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
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

    public CardDTO getCardDTO() {
        return cardDTO;
    }

    public void setCardDTO(CardDTO cardDTO) {
        this.cardDTO = cardDTO;
    }
}