package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class DiscardHandDownTo extends Action {
    private int cardsToDiscardDownTo;

    private List<Card> selectedCards = new ArrayList<>(2);

    public DiscardHandDownTo(int cardsToDiscardDownTo, String text) {
        this.cardsToDiscardDownTo = cardsToDiscardDownTo;
        this.text = text;
    }

    public int getCardsToDiscardDownTo() {
        return cardsToDiscardDownTo;
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_HAND) && !selectedCards.contains(card);
    }

    @Override
    public boolean processAction(Player player) {
        if (player.getHand().size() <= cardsToDiscardDownTo) {
            return false;
        } else {
            if (cardsToDiscardDownTo == player.getHand().size()) {
                player.getHand().forEach(player::addCardToDiscard);
                player.getHand().clear();
                player.addGameLog(player.getPlayerName() + " discarded " + player.getHand().size() + " numCardsToScrap");
                return false;
            } else {
                player.addGameLog(player.getPlayerName() + " is discarding down to " + cardsToDiscardDownTo + " numCardsToScrap");
                return true;
            }
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        selectedCards.forEach(player::discardCardFromHand);
    }
}
