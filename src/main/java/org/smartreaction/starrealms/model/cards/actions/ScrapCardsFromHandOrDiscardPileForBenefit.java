package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.ScrapCardsForBenefitAction;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class ScrapCardsFromHandOrDiscardPileForBenefit extends Action {
    int numCardsToScrap;
    private ScrapCardsForBenefitAction card;

    private List<Card> selectedCardsFromHand = new ArrayList<>(3);
    private List<Card> selectedCardsFromDiscard = new ArrayList<>(3);

    public ScrapCardsFromHandOrDiscardPileForBenefit(ScrapCardsForBenefitAction card, int numCardsToScrap, String text) {
        this.card = card;
        this.numCardsToScrap = numCardsToScrap;
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_HAND) || cardLocation.equals(Card.CARD_LOCATION_DISCARD);
    }

    @Override
    public boolean processAction(Player player) {
        if (player.getHand().isEmpty() && player.getDiscard().isEmpty()) {
            return false;
        } else {
            if (numCardsToScrap > player.getHand().size() + player.getDiscard().size()) {
                numCardsToScrap = player.getHand().size() + player.getDiscard().size();
            }
            return true;
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        selectedCardsFromHand.forEach(player::scrapCardFromHand);
        selectedCardsFromDiscard.forEach(player::scrapCardFromDiscard);
        card.cardsScrapped(player, getSelectedCards());
    }

    private List<Card> getSelectedCards() {
        List<Card> selectedCards = new ArrayList<>(selectedCardsFromHand);
        selectedCards.addAll(selectedCardsFromDiscard);
        return selectedCards;
    }

    public int getNumCardsToScrap() {
        return numCardsToScrap;
    }

    public List<Card> getSelectedCardsFromHand() {
        return selectedCardsFromHand;
    }

    public List<Card> getSelectedCardsFromDiscard() {
        return selectedCardsFromDiscard;
    }

    @Override
    public boolean showActionDialog() {
        return getSelectedCards().isEmpty();
    }
}
