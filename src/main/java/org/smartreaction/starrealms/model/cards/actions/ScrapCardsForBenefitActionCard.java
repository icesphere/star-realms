package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public interface ScrapCardsForBenefitActionCard {
    void cardsScrapped(Player player, List<Card> scrappedCards);
}
