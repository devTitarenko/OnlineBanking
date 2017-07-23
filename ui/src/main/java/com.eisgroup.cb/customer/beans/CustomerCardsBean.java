package com.eisgroup.cb.customer.beans;

import com.eisgroup.cb.model.Card;
import com.eisgroup.cb.model.Status;
import com.eisgroup.cb.service.CardService;
import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class CustomerCardsBean implements Serializable {
    @ManagedProperty(value = "#{cardService}")
    private CardService cardService;

    private Card card;
    private List<Card> cardList;
    private List<Card> filteredCardList;
    private Card selectedCard;
    private String selectedStatusName;
    private boolean isCardDetailButtonActivated;
    private boolean isBlockedButtonActivated;
    private boolean isUnblockButtonActivated;
    private boolean isRefreshButtonActivated;
    private Long customerId;

    private static final Logger LOGGER = Logger.getLogger(CustomerCardsBean.class);

    @PostConstruct
    public void init() {
        isRefreshButtonActivated = true;
        cardList = (List<Card>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cardsCustomerApp");
        filteredCardList = new ArrayList<>(cardList);
    }

    public void onRowSelect(SelectEvent event) {
        selectedCard = (Card) event.getObject();
        Status cardStatus = selectedCard.getStatus();
        isCardDetailButtonActivated = true;
        LOGGER.info(selectedCard);
        if ((Status.ACTIVE).equals(cardStatus)) {
            isBlockedButtonActivated = true;
            isUnblockButtonActivated = false;
        } else if ((Status.BLOCKED).equals(cardStatus)) {
            isUnblockButtonActivated = true;
            isBlockedButtonActivated = false;
        }
    }

    public void onRowUnselect(UnselectEvent event) {
        selectedCard = null;
        isCardDetailButtonActivated = false;
        isUnblockButtonActivated = false;
        isBlockedButtonActivated = false;
    }

    public void refreshPage() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String url = ((HttpServletRequest) context.getRequest()).getRequestURI() + "?i=1";
        context.redirect(url);
    }

    public void getSelectedCardsList(AjaxBehaviorEvent event) {
        LOGGER.info("Getting new filtered list");
        if (selectedStatusName.equals("All")) {
            LOGGER.info("Selected status is all");
            filteredCardList = new ArrayList<>(cardList);
        } else if (selectedStatusName.equals("ActiveAndBlocked")) {
            filteredCardList = new ArrayList<>();
            for (Card card : cardList) {
                if ((Status.ACTIVE).equals(card.getStatus()) || (Status.BLOCKED).equals(card.getStatus())) {
                    filteredCardList.add(card);
                }
            }
        } else {
            filteredCardList = new ArrayList<>();
            Status selectedStatus = Status.valueOf(selectedStatusName.toUpperCase());
            LOGGER.info("Selected status name " + selectedStatusName);
            for (Card card : cardList) {
                LOGGER.info("Card status " + card.getStatus().getName() + " selected status " + selectedStatus.getName());
                if (card.getStatus().equals(selectedStatus)) {
                    LOGGER.info("Adding card " + card.toString() + " to filtered list");
                    filteredCardList.add(card);
                }
            }
        }
        LOGGER.info("Card list: " + cardList);
        LOGGER.info("Filtered card list: " + filteredCardList);
    }

    public void blockCard() throws IOException {
        LOGGER.info("Selected card " + selectedCard);
        if ((Status.ACTIVE).equals(selectedCard.getStatus())) {
            selectedCard.setBlocked(true);
            cardService.save(selectedCard);
            refreshPage();
        }
    }

    public void unblockCard() throws IOException {
        LOGGER.info("Selected card " + selectedCard);
        if ((Status.BLOCKED).equals(selectedCard.getStatus())) {
            selectedCard.setBlocked(false);
            cardService.save(selectedCard);
            refreshPage();
        }
    }

    //Getters and setters
    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
    }

    public List<Card> getFilteredCardList() {
        return filteredCardList;
    }

    public void setFilteredCardList(List<Card> filteredCardList) {
        this.filteredCardList = filteredCardList;
    }

    public String getSelectedStatusName() {
        return selectedStatusName;
    }

    public void setSelectedStatusName(String selectedStatusName) {
        this.selectedStatusName = selectedStatusName;
    }

    public boolean isCardDetailButtonActivated() {
        return isCardDetailButtonActivated;
    }

    public void setCardDetailButtonActivated(boolean cardDetailButtonActivated) {
        isCardDetailButtonActivated = cardDetailButtonActivated;
    }

    public boolean isBlockedButtonActivated() {
        return isBlockedButtonActivated;
    }

    public void setBlockedButtonActivated(boolean blockedButtonActivated) {
        isBlockedButtonActivated = blockedButtonActivated;
    }

    public boolean isUnblockButtonActivated() {
        return isUnblockButtonActivated;
    }

    public void setUnblockButtonActivated(boolean unblockButtonActivated) {
        isUnblockButtonActivated = unblockButtonActivated;
    }

    public boolean isRefreshButtonActivated() {
        return isRefreshButtonActivated;
    }

    public void setRefreshButtonActivated(boolean refreshButtonActivated) {
        isRefreshButtonActivated = refreshButtonActivated;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
