package org.smartreaction.starrealms.model.cards;

import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public interface DiscardCardsForBenefitAction {
    void cardsDiscarded(Player player, List<Card> discardedCards);
}
