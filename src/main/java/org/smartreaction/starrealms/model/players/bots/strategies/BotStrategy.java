package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public interface BotStrategy {
    int getBuyCardScore(Card card, Player player);

    int getBuyScoreIncreaseForAverageTradeRowCost(int averageTradeRowCost, Card card, int deckNumber);
}
