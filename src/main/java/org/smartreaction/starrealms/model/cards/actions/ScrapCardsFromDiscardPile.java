package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class ScrapCardsFromDiscardPile extends Action {
    protected int numCardsToScrap;

    protected List<Card> selectedCards = new ArrayList<>(3);

    protected boolean optional;

    public ScrapCardsFromDiscardPile(int numCardsToScrap) {
        this.numCardsToScrap = numCardsToScrap;
        text = "Scrap " + numCardsToScrap + " card";
        if (numCardsToScrap != 1) {
            text += "s";
        }
    }

    public ScrapCardsFromDiscardPile(int numCardsToScrap, String text) {
        this.numCardsToScrap = numCardsToScrap;
        this.text = text;
    }

    public ScrapCardsFromDiscardPile(int numCardsToScrap, String text, boolean optional) {
        this.numCardsToScrap = numCardsToScrap;
        this.text = text;
        this.optional = optional;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_DISCARD);
    }

    @Override
    public boolean processAction(Player player) {
        return !player.getDiscard().isEmpty();
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        if (result.isDoneWithAction()) {
            selectedCards.forEach(player::scrapCardFromDiscard);
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
        return selectedCards.size() > 0 && (optional || selectedCards.size() == numCardsToScrap);
    }

    @Override
    public String getDoneText() {
        if (selectedCards.size() == 1) {
            return "Scrap " + selectedCards.get(0).getName();
        } else {
            return "Scrap " + selectedCards.size() + " cards";
        }
    }

    @Override
    public boolean isCardSelected(Card card) {
        return selectedCards.contains(card);
    }
}
