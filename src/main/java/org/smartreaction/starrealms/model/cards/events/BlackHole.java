package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.actions.DiscardCardsForBenefitActionCard;
import org.smartreaction.starrealms.model.cards.actions.DiscardCardsFromHandForBenefit;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class BlackHole extends Event implements DiscardCardsForBenefitActionCard {
    public BlackHole() {
        name = "Black Hole";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player may discard up to two cards. For each card less than two that a player discards, that player loses 4 Authority";
    }

    @Override
    public void handleEvent(Player player) {
        player.addAction(new DiscardCardsFromHandForBenefit(this, 2, "Discard up to two cards. For each card less than two, lose 4 Authority.", true));
        player.getOpponent().addAction(new DiscardCardsFromHandForBenefit(this, 2, "Discard up to two cards. For each card less than two, lose 4 Authority.", true));
    }

    @Override
    public void cardsDiscarded(Player player, List<Card> discardedCards) {
        if (discardedCards.size() == 1) {
            if (player.getAuthority() <= 4) {
                player.setAuthority(1);
            } else {
                player.reduceAuthority(4);
            }
        }
    }

    @Override
    public void onChoseDoNotUse(Player player) {
        if (player.getAuthority() <= 8) {
            player.setAuthority(1);
        } else {
            player.reduceAuthority(8);
        }
    }
}
