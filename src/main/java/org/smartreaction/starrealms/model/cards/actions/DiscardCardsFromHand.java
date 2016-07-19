package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class DiscardCardsFromHand extends Action {
    private int numCardsToDiscard;

    private List<Card> selectedCards = new ArrayList<>(3);

    private boolean optional;

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

    public int getNumCardsToDiscard() {
        return numCardsToDiscard;
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
        if (player.getHand().isEmpty()) {
            return false;
        } else {
            if (numCardsToDiscard > player.getHand().size()) {
                player.getHand().stream().forEach(player::addCardToDiscard);
                player.getHand().clear();
                player.addGameLog(player.getPlayerName() + " discarded " + player.getHand().size() + " numCardsToScrap");
                return false;
            } else {
                player.addGameLog(player.getPlayerName() + " is discarding " + numCardsToDiscard + " numCardsToScrap");
                return true;
            }
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        selectedCards.stream().forEach(player::discardCardFromHand);
    }

    @Override
    public boolean showActionDialog() {
        return selectedCards.size() == 0;
    }

    public boolean isOptional() {
        return optional;
    }
}
