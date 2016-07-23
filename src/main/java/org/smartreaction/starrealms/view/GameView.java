package org.smartreaction.starrealms.view;

import org.apache.commons.lang3.StringUtils;
import org.smartreaction.starrealms.model.ChatMessage;
import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.Action;
import org.smartreaction.starrealms.model.cards.actions.ActionResult;
import org.smartreaction.starrealms.model.cards.actions.ScrapCardsFromDiscardPile;
import org.smartreaction.starrealms.model.cards.actions.ScrapCardsFromHandOrDiscardPile;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.events.Event;
import org.smartreaction.starrealms.model.players.Player;
import org.smartreaction.starrealms.service.GameService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class GameView implements Serializable {
    @ManagedProperty(value="#{userSession}")
    UserSession userSession;

    @EJB
    GameService gameService;

    Card cardToView;

    String chatMessage = "";

    boolean showingCards;

    String showingCardsTitle;

    List<Card> cardsToShow;

    String cardsToShowSource;

    public void sendGameMessageToAll(String message) {
        sendGameMessage("*", message);
    }

    public void sendGameMessageToPlayer(String message) {
        sendGameMessage(getPlayer().getPlayerName(), message);
    }

    public void sendGameMessageToOpponent(String message) {
        sendGameMessage(getPlayer().getOpponent().getPlayerName(), message);
    }

    public void sendGameMessage(String recipient, String message) {
        gameService.sendGameMessage(getPlayer().getPlayerName(), recipient, getGame().getGameId(), message);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public Game getGame() {
        return userSession.getUser().getCurrentGame();
    }

    public Player getPlayer() {
        return userSession.getUser().getCurrentPlayer();
    }

    public Player getOpponent() {
        return getPlayer().getOpponent();
    }

    public Action getAction() {
        return getPlayer().getCurrentAction();
    }

    public String getTradeRowCardClass(Card card) {
        String cardClass = "";

        if (highlightTradeRowCard(card)) {
            cardClass = "buyableCard";
        }

        Action action = getAction();
        if (action != null && action.isCardSelected(card)) {
            cardClass += " selected";
        }

        return cardClass;
    }

    public boolean highlightTradeRowCard(Card card) {
        if (!getPlayer().isYourTurn()) {
            return false;
        } else if (getAction() != null) {
            return getAction().isCardActionable(card, Card.CARD_LOCATION_TRADE_ROW, getPlayer());
        } else {
            return getPlayer().isCardBuyable(card);
        }
    }

    public String getActionableCardClass(Card card, String source) {
        String cardClass = getCardClass(card);

        if (highlightCard(card, source)) {
            cardClass += " actionableCard";
        }

        Action action = getAction();
        if (action != null && action.isCardSelected(card)) {
            cardClass += " selected";
        }

        return cardClass;
    }

    public boolean highlightCard(Card card, String cardLocation) {
        if (!getPlayer().isYourTurn()) {
            return false;
        } else if (getAction() != null) {
            return getAction().isCardActionable(card, cardLocation, getPlayer());
        } else {
            return card.isActionable(getPlayer(), cardLocation);
        }
    }

    public String getCardClass(Card card) {
        String cardClass = "";
        
        if (card.getFaction() == Faction.BLOB) {
            cardClass = "blob";
        } else if (card.getFaction() == Faction.MACHINE_CULT) {
            cardClass = "machineCult";
        } else if (card.getFaction() == Faction.STAR_EMPIRE) {
            cardClass = "starEmpire";
        } else if (card.getFaction() == Faction.TRADE_FEDERATION) {
            cardClass = "tradeFederation";
        } else if (card.getFaction() == Faction.UNALIGNED) {
            cardClass = "unaligned";
        } else if (card instanceof Event) {
            cardClass = "event";
        }

        return cardClass;
    }

    public String getFactionDisplayName(Faction faction) {
        switch (faction) {
            case BLOB:
                return "Blob";
            case MACHINE_CULT:
                return "Machine Cult";
            case STAR_EMPIRE:
                return "Star Empire";
            case TRADE_FEDERATION:
                return "Trade Federation";
            default:
                return "";
        }
    }

    public List<Card> getCardsForPlayArea() {
        return getGame().getCurrentPlayer().getInPlay().stream().filter(c -> !c.isBase() && !c.isHero()).collect(Collectors.toList());
    }

    public void updateCardView() {
        Map<String, String> paramValues = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String cardName = paramValues.get("cardName");
        cardToView = gameService.getCardByName(cardName);
    }

    public void cardClicked() {
        if (!getPlayer().isYourTurn()) {
            return;
        }

        Map<String, String> paramValues = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String source = paramValues.get("source");

        if (source.equals("explorers") || source.equals("tradeRow")) {
            Card card;
            if (source.equals("explorers")) {
                card = gameService.getCardByName("explorer");
            } else {
                String cardId = paramValues.get("cardId");
                card = findCardById(getGame().getTradeRow(), cardId);
            }
            if (highlightTradeRowCard(card)) {
                if (getAction() != null) {
                    handleCardClickedForAction(card, source);
                } else {
                    getPlayer().buyCard(card);
                    sendGameMessageToAll("refresh_game_page");
                }
            }
        } else  {
            String cardId = paramValues.get("cardId");

            if (source.equals(Card.CARD_LOCATION_HAND)) {
                Card card = findCardById(getPlayer().getHand(), cardId);
                if (highlightCard(card, source)) {
                    if (getAction() != null) {
                        handleCardClickedForAction(card, source);
                    } else {
                        getPlayer().playCard(card);
                        refreshGamePageWithCheckForAction();
                    }
                }
            } else if (source.equals(Card.CARD_LOCATION_DISCARD)) {
                Card card = findCardById(getPlayer().getDiscard(), cardId);
                if (highlightCard(card, source)) {
                    if (getAction() != null) {
                        handleCardClickedForAction(card, source);
                    }
                }
            } else if (source.equals(Card.CARD_LOCATION_PLAY_AREA)) {
                Card card = findCardById(getCardsForPlayArea(), cardId);
                if (highlightCard(card, source)) {
                    if (getAction() != null) {
                        handleCardClickedForAction(card, source);
                    } else if (card instanceof AlliableCard) {
                        getPlayer().useAlliedAbility((AlliableCard) card);
                        sendGameMessageToAll("refresh_game_page");
                    }
                }
            } else if (source.equals(Card.CARD_LOCATION_PLAYER_BASES) || source.equals(Card.CARD_LOCATION_OPPONENT_BASES)) {
                Player player;
                if (source.equals("opponentBases")) {
                    player = getOpponent();
                } else {
                    player = getPlayer();
                }
                Card card = findCardById(player.getBases(), cardId);
                if (highlightCard(card, source)) {
                    if (getAction() != null) {
                        handleCardClickedForAction(card, source);
                    } else {
                        ((Base) card).useBase(player);
                        sendGameMessageToAll("refresh_middle_section");
                        sendGameMessageToAll("refresh_right_section");
                    }
                }
            }
        }

        checkForAction();
    }

    public Card findCardById(List<? extends Card> cards, String cardId) {
        for (Card card : cards) {
            if (card.getId().equals(cardId)) {
                return card;
            }
        }
        return null;
    }

    public void handleCardClickedForAction(Card card, String cardLocation) {
        Action action = getAction();
        ActionResult result = new ActionResult();
        result.setCardLocation(cardLocation);

        result.getSelectedCards().add(card);

        getPlayer().actionResult(action, result);

        refreshGamePageWithCheckForAction();
    }

    public Card getCardToView() {
        return cardToView;
    }

    public void setCardToView(Card cardToView) {
        this.cardToView = cardToView;
    }

    public void playAll() {
        getPlayer().playAll();
        sendGameMessageToOpponent("refresh_game_page");
        checkForAction();
    }

    public void endTurn() {
        if (getAction() != null) {
            sendShowActionToPlayer();
        } else {
            getPlayer().endTurn();
            refreshAfterEndTurn();
        }
    }

    public void refreshAfterEndTurn() {
        if (!getGame().isGameOver()) {
            getPlayer().getOpponent().startTurn();
        }
        if (getPlayer().getOpponent().getCurrentAction() != null) {
            sendGameMessageToOpponent("refresh_game_page");
        } else {
            sendGameMessageToOpponent("refresh_game_page");
        }
    }

    public void choiceMade(int choiceSelected) {
        ActionResult result = new ActionResult();
        result.setChoiceSelected(choiceSelected);
        getPlayer().actionResult(getAction(), result);
        refreshGamePageWithCheckForAction();
    }

    public void refreshGamePageWithCheckForAction() {
        if (getAction() != null) {
            sendShowActionToPlayer();
            sendGameMessageToOpponent("refresh_game_page");
        } else {
            if (!getPlayer().isYourTurn()) {
                refreshAfterEndTurn();
            } else {
                sendGameMessageToAll("refresh_game_page");
            }
        }
    }

    public void checkForAction() {
        if (getAction() != null) {
            sendShowActionToPlayer();
        }
    }

    public void sendShowActionToPlayer() {
        sendGameMessageToPlayer("refresh_game_page");
    }

    public String quitGame() {
        getGame().quitGame(getPlayer());
        sendGameMessageToOpponent("refresh_game_page");
        return exitGame();
    }

    public String exitGame() {
        userSession.getUser().setCurrentGame(null);
        userSession.getUser().setCurrentPlayer(null);

        gameService.refreshLobby(userSession.getUser().getUsername());

        return "lobby.xhtml?faces-redirect=true";
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void sendChatMessage() {
        if (!StringUtils.isEmpty(chatMessage)) {
            getGame().getChatMessages().add(new ChatMessage(getPlayer().getPlayerName(), chatMessage));
            chatMessage = "";
            sendGameMessageToOpponent("refresh_chat");
        }
    }

    public void attackOpponent() {
        getPlayer().attackOpponentWithRemainingCombat();
        sendGameMessageToAll("refresh_middle_section");
    }

    public void showCards(List<Card> cards, String title, String source) {
        if (!cards.isEmpty()) {
            showingCards = true;
            showingCardsTitle = title;
            cardsToShow = cards;
            cardsToShowSource = source;

            sendGameMessageToPlayer("refresh_middle_section");
        }
    }

    public void hideCardsToShow() {
        showingCards = false;
        cardsToShow = null;
    }

    public boolean isShowingCards() {
        return showingCards;
    }

    public List<Card> getCardsToShow() {
        return cardsToShow;
    }

    public String getShowingCardsTitle() {
        return showingCardsTitle;
    }

    public String getCardsToShowSource() {
        return cardsToShowSource;
    }

    public void doNotUseAction() {
        getPlayer().actionResult(getAction(), ActionResult.doNotUseActionResult());
    }

    public void doneWithAction() {
        getPlayer().actionResult(getAction(), ActionResult.doneWithActionResult());
        sendGameMessageToAll("refresh_game_page");
    }

    public void scrapCard(Card card) {
        getPlayer().scrapCardInPlayForBenefit(card);
        sendGameMessageToAll("refresh_game_page");
    }

    public void attackOpponentBase(Base base) {
        getPlayer().destroyOpponentBase(base);
        sendGameMessageToAll("refresh_game_page");
    }

    public boolean isBaseAttackable(Base base) {
        return getPlayer().isYourTurn() && getPlayer().getCombat() >= base.getShield()
                && (getOpponent().getOutposts().isEmpty() || base.isOutpost());
    }

    public boolean isShowScrapLink(Card card, String cardLocation) {
        return getPlayer().isYourTurn() && card.isScrappable() &&
                (cardLocation.equals(Card.CARD_LOCATION_PLAY_AREA) ||
                        cardLocation.equals(Card.CARD_LOCATION_PLAYER_BASES) ||
                        cardLocation.equals(Card.CARD_LOCATION_PLAYER_HEROES));
    }

    public boolean isHighlightDiscardButton() {
        Action action = getAction();
        return action != null && getPlayer().isYourTurn() && !getPlayer().getDiscard().isEmpty()
                && (action instanceof ScrapCardsFromHandOrDiscardPile || action instanceof ScrapCardsFromDiscardPile);
    }
}
