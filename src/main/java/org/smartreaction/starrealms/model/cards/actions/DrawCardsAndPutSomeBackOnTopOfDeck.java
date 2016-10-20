package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class DrawCardsAndPutSomeBackOnTopOfDeck extends Action {
    protected int numCardsToDraw;

    protected int numCardsToPutBack;

    protected List<Card> cardsDrawn;

    protected List<Card> selectedCards = new ArrayList<>(3);

    public DrawCardsAndPutSomeBackOnTopOfDeck(int numCardsToDraw, int numCardsToPutBack) {
        this.numCardsToDraw = numCardsToDraw;
        this.numCardsToPutBack = numCardsToPutBack;
        this.text = "You have drawn " + numCardsToDraw + " cards and need to put " + numCardsToPutBack + " of those cards back on top of your deck";
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_HAND) && cardsDrawn.contains(card);
    }

    @Override
    public boolean processAction(Player player) {
        cardsDrawn = player.drawCards(numCardsToDraw);
        if (cardsDrawn.size() < 3) {
            cardsDrawn.forEach(c -> {
                player.getHand().remove(c);
                player.addCardToTopOfDeck(c, false);
            });
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        if (result.isDoneWithAction()) {
            selectedCards.forEach(c -> {
                player.getHand().remove(c);
                player.addCardToTopOfDeck(c, false);
            });
            return true;
        } else {
            Card selectedCard = result.getSelectedCard();
            if (selectedCards.contains(selectedCard)) {
                selectedCards.remove(selectedCard);
            } else {
                selectedCards.add(selectedCard);
            }
        }

        return false;
    }

    @Override
    public boolean isShowDone() {
        return selectedCards.size() > 0 && selectedCards.size() <= numCardsToPutBack && selectedCards.size() == numCardsToPutBack;
    }

    @Override
    public boolean isCardSelected(Card card) {
        return selectedCards.contains(card);
    }
}
