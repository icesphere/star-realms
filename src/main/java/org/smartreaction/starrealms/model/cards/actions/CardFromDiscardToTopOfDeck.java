package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class CardFromDiscardToTopOfDeck extends Action implements SelectFromDiscardAction {
    public CardFromDiscardToTopOfDeck(String text) {
        this.text = text;
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
        Card card = result.getSelectedCard();
        player.getDiscard().remove(card);
        player.addCardToTopOfDeck(card);
        return true;
    }

    @Override
    public boolean isShowDoNotUse() {
        return true;
    }
}
