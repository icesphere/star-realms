package org.smartreaction.starrealms.model.cards;

import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public interface ScrapCardsForBenefitAction {
    void cardsScrapped(Player player, List<Card> scrappedCards);
}
