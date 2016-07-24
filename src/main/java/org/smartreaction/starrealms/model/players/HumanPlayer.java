package org.smartreaction.starrealms.model.players;

import org.smartreaction.starrealms.model.User;
import org.smartreaction.starrealms.model.cards.Card;

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
    public Card chooseCardFromDiscardToAddToTopOfDeck() {
        return null;
    }

    @Override
    public void drawCardsAndPutSomeBackOnTop(int cardsToDraw, int cardsToPutBack) {

    }

    @Override
    public void handleDeathWorld() {

    }
}
