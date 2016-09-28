package org.smartreaction.starrealms.model.players;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.User;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.actions.*;
import org.smartreaction.starrealms.model.cards.events.Event;

public class HumanPlayer extends Player {
    private User user;

    public HumanPlayer(User user) {
        this.user = user;
        playerName = user.getUsername();
    }

    public User getUser() {
        return user;
    }

    @Override
    public void takeTurn() {

    }

    private void addAction(Action action) {
        actionsQueue.add(action);
        if (yourTurn && currentAction == null) {
            resolveActions();
        }
    }

    @Override
    public void destroyTargetBase() {
        addAction(new DestroyOpponentBase("Destroy target base"));
    }

    @Override
    public void optionallyScrapCardsFromHandOrDiscard(int cards) {
        addAction(new ScrapCardsFromHandOrDiscardPile(cards, "Scrap up to " + cards + " from your hand or discard pile", true));
    }

    @Override
    public void optionallyScrapCardsFromHandOrDiscardForBenefit(ScrapCardsForBenefitActionCard card, int numCardsToScrap, String text) {
        addAction(new ScrapCardsFromHandOrDiscardPileForBenefit(card, numCardsToScrap, text, true));
    }

    @Override
    public void optionallyScrapCardsFromHandOrDiscardOrTradeRow(int cards) {
        addAction(new ScrapCardsFromHandOrDiscardPileOrTradeRow(cards, "Scrap up to " + cards + " from your hand or discard pile or the trade row", true));
    }

    @Override
    public void optionallyDiscardCardsForBenefit(DiscardCardsForBenefitActionCard card, int numCardsToDiscard, String text) {
        addAction(new DiscardCardsFromHandForBenefit(card, numCardsToDiscard, text, true));
    }

    @Override
    public void discardCardsForBenefit(DiscardCardsForBenefitActionCard card, int numCardsToDiscard, String text) {
        addAction(new DiscardCardsFromHandForBenefit(card, numCardsToDiscard, text));
    }

    @Override
    public void optionallyScrapCardsInTradeRow(int cards) {
        addAction(new ScrapCardsFromTradeRow(cards, true));
    }

    @Override
    public void makeChoice(ChoiceActionCard card, Choice... choices) {
        addAction(new ChoiceAction(card, choices));
    }

    public void makeChoice(ChoiceActionCard card, String text, Choice... choices) {
        addAction(new ChoiceAction(card, text, choices));
    }

    @Override
    public void scrapCardFromHand(boolean optional) {
        if (optional) {
            addAction(new ScrapCardsFromHand(1, "You may scrap a card from your hand", true));
        } else {
            addAction(new ScrapCardsFromHand(1, "Scrap a card from your hand", false));
        }
    }

    @Override
    public void optionallyScrapCardsFromDiscard(int cards) {
        addAction(new ScrapCardsFromDiscardPile(cards, "You may scrap a card from your discard pile", true));
    }

    @Override
    public void returnTargetBaseToHand() {
        addAction(new ReturnBaseToHand("Return a base to owner's hand"));
    }

    @Override
    public void acquireFreeCard(int maxCost) {
        addAction(new FreeCardFromTradeRow(maxCost, "Acquire a free card from the trade row costing up to " + maxCost));
    }

    @Override
    public void acquireFreeCardToTopOfDeck(int maxCost) {
        addAction(new FreeCardFromTradeRow(maxCost, "Acquire a free card from the trade row to the top of your deck costing up to " + maxCost, Card.CARD_LOCATION_DECK));
    }

    @Override
    public void acquireFreeShipToTopOfDeck(Integer maxCost) {
        String text;
        if (maxCost != null) {
            text = "Choose a free ship costing up to " + maxCost + " from the trade row to put on top of your deck";
        } else {
            text = "Choose a free ship from the trade row to put on top of your deck";
        }
        addAction(new FreeCardFromTradeRow(maxCost, text, Card.CARD_LOCATION_DECK, true));
    }

    @Override
    public void acquireFreeCardToHand(int maxCost) {
        addAction(new FreeCardFromTradeRow(maxCost, "Acquire a free card from the trade row to your hand costing up to " + maxCost, Card.CARD_LOCATION_HAND));
    }

    @Override
    public void drawCardsAndPutSomeBackOnTop(int cardsToDraw, int cardsToPutBack) {
        addAction(new DrawCardsAndPutSomeBackOnTopOfDeck(cardsToDraw, cardsToPutBack));
    }

    @Override
    public void discardCardsFromHand(int cards) {
        addAction(new DiscardCardsFromHand(cards));
    }

    @Override
    public void destroyOwnBase(DestroyOwnBaseActionCard card, String text) {
        addAction(new DestroyOwnBase(card, text));
    }

    @Override
    public void addCardAction(CardActionCard card, String text) {
        addAction(new CardAction(card, text));
    }

    @Override
    public void showTriggeredEvent(Event event) {
        addAction(new ShowTriggeredEvent(event));
    }

    @Override
    public void addCardFromDiscardToTopOfDeck(Integer maxCost) {
        addAction(new CardFromDiscardToTopOfDeck(maxCost));
    }
}
