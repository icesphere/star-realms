package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class DiscardCardsFromHand extends Action {
    protected int numCardsToDiscard;

    protected List<Card> selectedCards = new ArrayList<>(3);

    protected boolean optional;

    public DiscardCardsFromHand(int numCardsToDiscard) {
        this.numCardsToDiscard = numCardsToDiscard;
        text = "Discard " + numCardsToDiscard + " card";
        if (numCardsToDiscard != 1) {
            text += "s";
        }
    }

    public DiscardCardsFromHand(int numCardsToDiscard, String text) {
        this.numCardsToDiscard = numCardsToDiscard;
        this.text = text;
    }

    public DiscardCardsFromHand(int numCardsToDiscard, String text, boolean optional) {
        this.numCardsToDiscard = numCardsToDiscard;
        this.text = text;
        this.optional = optional;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_HAND);
    }

    @Override
    public boolean processAction(Player player) {
        return !player.getHand().isEmpty();
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        if (result.isDoneWithAction()) {
            selectedCards.forEach(player::discardCardFromHand);
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
    public boolean isShowDoNotUse() {
        return optional;
    }

    @Override
    public boolean isShowDone() {
        //todo handle if not optional and available cards < numCardsToDiscard
        return selectedCards.size() > 0 && selectedCards.size() <= numCardsToDiscard && (optional || selectedCards.size() == numCardsToDiscard);
    }

    @Override
    public String getDoneText() {
        if (selectedCards.size() == 1) {
            return "Discard " + selectedCards.get(0).getName();
        } else {
            return "Discard " + selectedCards.size() + " cards";
        }
    }

    @Override
    public boolean isCardSelected(Card card) {
        return selectedCards.contains(card);
    }
}
