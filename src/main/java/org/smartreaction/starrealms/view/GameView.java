package org.smartreaction.starrealms.view;

import org.apache.commons.lang3.StringUtils;
import org.smartreaction.starrealms.model.ChatMessage;
import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.Action;
import org.smartreaction.starrealms.model.cards.actions.ActionResult;
import org.smartreaction.starrealms.model.cards.actions.SelectFromDiscardAction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.events.Event;
import org.smartreaction.starrealms.model.cards.gambits.Gambit;
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

import static org.smartreaction.starrealms.model.cards.Faction.*;

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
        if (card == null) {
            new IllegalArgumentException("Error highlighting card for location: " + cardLocation).printStackTrace();
            return false;
        }
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
        if (card instanceof Event) {
            cardClass = "event";
        } else if (card instanceof Gambit) {
            cardClass = "gambit";
        } else if (card.getFactions().size() == 0) {
            cardClass = "unaligned";
        } else if (card.getFactions().size() == 1) {
            if (card.hasFaction(Faction.BLOB)) {
                cardClass = "blob";
            } else if (card.hasFaction(MACHINE_CULT)) {
                cardClass = "machineCult";
            } else if (card.hasFaction(STAR_EMPIRE)) {
                cardClass = "starEmpire";
            } else if (card.hasFaction(TRADE_FEDERATION)) {
                cardClass = "tradeFederation";
            }
        } else {
            if (card.hasFaction(Faction.BLOB) && card.hasFaction(MACHINE_CULT)) {
                return "blob-machineCult";
            } else if (card.hasFaction(Faction.BLOB) && card.hasFaction(STAR_EMPIRE)) {
                return "blob-starEmpire";
            } else if (card.hasFaction(Faction.BLOB) && card.hasFaction(TRADE_FEDERATION)) {
                return "blob-tradeFederation";
            } else if (card.hasFaction(TRADE_FEDERATION) && card.hasFaction(STAR_EMPIRE)) {
                return "tradeFederation-starEmpire";
            } else if (card.hasFaction(TRADE_FEDERATION) && card.hasFaction(MACHINE_CULT)) {
                return "tradeFederation-machineCult";
            } else if (card.hasFaction(MACHINE_CULT) && card.hasFaction(STAR_EMPIRE)) {
                return "machineCult-starEmpire";
            }
        }

        return cardClass;
    }

    public String getFactionDisplayName(Card card) {
        if (card.getFactions().size() == 1) {
            if (card.hasFaction(BLOB)) {
                return "Blob";
            } else if (card.hasFaction(MACHINE_CULT)) {
                return "Machine Cult";
            } else if (card.hasFaction(STAR_EMPIRE)) {
                return "Star Empire";
            } else if (card.hasFaction(TRADE_FEDERATION)) {
                return "Trade Federation";
            }
        } else if (card.getFactions().size() == 2) {
            if (card.hasFaction(Faction.BLOB) && card.hasFaction(MACHINE_CULT)) {
                return "Blob / Machine Cult";
            } else if (card.hasFaction(Faction.BLOB) && card.hasFaction(STAR_EMPIRE)) {
                return "Blob / Star Empire";
            } else if (card.hasFaction(Faction.BLOB) && card.hasFaction(TRADE_FEDERATION)) {
                return "Blob / Trade Federation";
            } else if (card.hasFaction(TRADE_FEDERATION) && card.hasFaction(STAR_EMPIRE)) {
                return "Trade Federation / Star Empire";
            } else if (card.hasFaction(TRADE_FEDERATION) && card.hasFaction(MACHINE_CULT)) {
                return "Trade Federation / Machine Cult";
            } else if (card.hasFaction(MACHINE_CULT) && card.hasFaction(STAR_EMPIRE)) {
                return "Machine Cult / Star Empire";
            }
        }

        return "";
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
                    refreshGamePageForAll();
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
                        refreshGamePageForAll();
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
                    } else if (card.isAlliableCard()) {
                        getPlayer().useAlliedAbilities((AlliableCard) card);
                        refreshGamePageForAll();
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

        result.setSelectedCard(card);

        getPlayer().actionResult(action, result);

        refreshGamePageForAll();
    }

    public Card getCardToView() {
        return cardToView;
    }

    public void setCardToView(Card cardToView) {
        this.cardToView = cardToView;
    }

    public void playAll() {
        getPlayer().playAll();
        refreshGamePageForOpponent();
        checkForAction();
    }

    public void endTurn() {
        if (getAction() != null) {
            sendShowActionToPlayer();
        } else {
            getPlayer().endTurn();
            refreshGamePageForAll();
        }
    }

    public void choiceMade(int choiceSelected) {
        ActionResult result = new ActionResult();
        result.setChoiceSelected(choiceSelected);
        getPlayer().actionResult(getAction(), result);
        refreshGamePageForAll();
    }

    public void checkForAction() {
        if (getAction() != null) {
            sendShowActionToPlayer();
        }
    }

    public void sendShowActionToPlayer() {
        refreshGamePageForPlayer();
    }

    public String quitGame() {
        getGame().quitGame(getPlayer());
        refreshGamePageForOpponent();
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
        showingCards = true;
        showingCardsTitle = title;
        cardsToShow = cards;
        cardsToShowSource = source;

        sendGameMessageToPlayer("refresh_middle_section");
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
        refreshGamePageForPlayer();
    }

    public void doneWithAction() {
        getPlayer().actionResult(getAction(), ActionResult.doneWithActionResult());
        refreshGamePageForAll();
    }

    public void scrapCard(Card card) {
        getPlayer().scrapCardInPlayForBenefit(card);
        refreshGamePageForAll();
    }

    public boolean isOpponentAttackable() {
        return getPlayer().isYourTurn() && getPlayer().getCombat() > 0 && getOpponent().getOutposts().isEmpty();
    }

    public void attackOpponentBase(Base base) {
        getPlayer().destroyOpponentBase(base);
        refreshGamePageForAll();
    }
    
    public void refreshGamePageForAll() {
        sendGameMessageToAll("refresh_game_page");
    }

    public void refreshGamePageForPlayer() {
        sendGameMessageToPlayer("refresh_game_page");
    }

    public void refreshGamePageForOpponent() {
        sendGameMessageToOpponent("refresh_game_page");
    }

    public boolean isBaseAttackable(Base base) {
        return getPlayer().isYourTurn() && getPlayer().getCombat() >= base.getShield()
                && (getOpponent().getOutposts().isEmpty() || base.isOutpost());
    }

    public boolean isShowScrapLink(Card card, String cardLocation) {
        return getPlayer().isYourTurn() && card.isScrappable() &&
                (cardLocation.equals(Card.CARD_LOCATION_PLAY_AREA) ||
                        cardLocation.equals(Card.CARD_LOCATION_PLAYER_BASES) ||
                        cardLocation.equals(Card.CARD_LOCATION_PLAYER_HEROES) ||
                        cardLocation.equals(Card.CARD_LOCATION_PLAYER_GAMBITS));
    }

    public boolean isHighlightDiscardButton() {
        Action action = getAction();
        return action != null && getPlayer().isYourTurn() && !getPlayer().getDiscard().isEmpty()
                && (action instanceof SelectFromDiscardAction);
    }
}
