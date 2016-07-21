package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class ScrapCardsFromDiscardPile extends Action {
    int numCardsToScrap;

    private List<Card> selectedCards = new ArrayList<>(3);

    public ScrapCardsFromDiscardPile(int numCardsToScrap, String text) {
        this.numCardsToScrap = numCardsToScrap;
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_DISCARD);
    }

    @Override
    public boolean processAction(Player player) {
        if (player.getHand().isEmpty()) {
            return false;
        } else {
            if (numCardsToScrap > player.getHand().size()) {
                numCardsToScrap = player.getHand().size();
            }
            return true;
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        selectedCards.forEach(player::scrapCardFromDiscard);
    }

    public int getNumCardsToScrap() {
        return numCardsToScrap;
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
    }
}
