package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class DiscardCardsFromHandForBenefit extends Action {
    private DiscardCardsForBenefitActionCard card;
    private int numCardsToDiscard;

    private List<Card> selectedCards = new ArrayList<>();

    public DiscardCardsFromHandForBenefit(DiscardCardsForBenefitActionCard card, int numCardsToDiscard, String text) {
        this.card = card;
        this.numCardsToDiscard = numCardsToDiscard;
        this.text = text;
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
                numCardsToDiscard = player.getHand().size();
            }
            return true;
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        selectedCards.forEach(player::scrapCardFromHand);
        card.cardsDiscarded(player, selectedCards);
    }

    public int getNumCardsToDiscard() {
        return numCardsToDiscard;
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
    }

    @Override
    public boolean showActionDialog() {
        return selectedCards.isEmpty();
    }
}
