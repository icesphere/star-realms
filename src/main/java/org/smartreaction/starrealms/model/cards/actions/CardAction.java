package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class CardAction extends Action {
    private Card cardActionCard;

    private List<Card> selectedCards = new ArrayList<>(2);

    public CardAction(Card cardActionCard, String text) {
        this.cardActionCard = cardActionCard;
        this.text = text;
    }

    public Card getCardActionCard() {
        return cardActionCard;
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return false;
    }

    @Override
    public boolean processAction(Player player) {
        return true;
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
    }

    @Override
    public boolean showActionDialog() {
        return selectedCards.size() == 0;
    }
}
