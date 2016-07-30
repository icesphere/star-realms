package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class CardAction extends Action {
    private CardActionCard cardActionCard;

    public CardAction(CardActionCard cardActionCard, String text) {
        this.cardActionCard = cardActionCard;
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardActionCard.isCardActionable(card, this, cardLocation, player);
    }

    @Override
    public boolean processAction(Player player) {
        return cardActionCard.processCardAction(player);
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        cardActionCard.processCardActionResult(this, player, result);
        return true;
    }

    public CardActionCard getCardActionCard() {
        return cardActionCard;
    }
}
