package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class ScrapCardsFromHandForBenefit extends Action {
    private ScrapCardsForBenefitActionCard card;
    private int numCardsToScrap;

    private List<Card> selectedCards = new ArrayList<>();

    public ScrapCardsFromHandForBenefit(ScrapCardsForBenefitActionCard card, int numCardsToScrap, String text) {
        this.card = card;
        this.numCardsToScrap = numCardsToScrap;
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
            List<Card> cardsAvailableToScrap = new ArrayList<>(player.getHand());
            if (numCardsToScrap > cardsAvailableToScrap.size()) {
                cardsAvailableToScrap.stream().forEach(player::scrapCardFromHand);
                card.cardsScrapped(player, cardsAvailableToScrap);
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        selectedCards.forEach(player::scrapCardFromHand);
        card.cardsScrapped(player, selectedCards);
    }

    public int getNumCardsToScrap() {
        return numCardsToScrap;
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
    }

    @Override
    public boolean showActionDialog() {
        return selectedCards.isEmpty();
    }
}
