package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class CardFromDiscardToTopOfDeck extends Action implements SelectFromDiscardAction {
    Integer maxCost;

    public CardFromDiscardToTopOfDeck(Integer maxCost) {
        text = "Choose a card from your discard pile ";
        if (maxCost != null) {
            text += "of cost " + maxCost + " or less ";
        }
        text += "to put on top of your deck";
        this.maxCost = maxCost;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_DISCARD) && (maxCost == null || card.getCost() <= maxCost);
    }

    @Override
    public boolean processAction(Player player) {
        if (maxCost == null) {
            return !player.getDiscard().isEmpty();
        } else {
            return player.getDiscard().stream().anyMatch(c -> c.getCost() <= maxCost);
        }
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
