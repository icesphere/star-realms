package org.smartreaction.starrealms.model.players;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.User;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.ships.Ship;

import java.util.List;

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
    public void makeChoice(Card card, Choice... choices) {

    }

    @Override
    public Ship getShipToCopy() {
        return null;
    }

    @Override
    public Base getBaseToCopy() {
        return null;
    }

    @Override
    public Faction chooseFactionForCard(Card card) {
        return null;
    }

    @Override
    public Card chooseCardFromDiscardToAddToTopOfDeck() {
        return null;
    }

    @Override
    public void handleBlackHole() {

    }

    @Override
    public void handleBombardment() {

    }

    @Override
    public void drawCardsAndPutSomeBackOnTop(int cardsToDraw, int cardsToPutBack) {

    }

    @Override
    public void handleDeathWorld() {

    }

    @Override
    public List<Card> getCardsToDiscardForSupplyDepot() {
        return null;
    }

    @Override
    public List<Card> getCardsToDiscard(int cards, boolean optional) {
        return null;
    }
}
