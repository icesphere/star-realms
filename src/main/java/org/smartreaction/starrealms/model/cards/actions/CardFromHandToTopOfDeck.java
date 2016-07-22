package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class CardFromHandToTopOfDeck extends Action {
    public CardFromHandToTopOfDeck(String text) {
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_HAND);
    }

    @Override
    public boolean processAction(Player player) {
        if (player.getHand().isEmpty()) {
            return false;
        } else {
            player.addGameLog(player.getPlayerName() + " is putting a card from their hand on top of their deck");
            return true;
        }
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        Card card = result.getSelectedCard();
        player.getHand().remove(card);
        player.addCardToTopOfDeck(card);
        return true;
    }

    @Override
    public boolean isShowDoNotUse() {
        return true;
    }
}
