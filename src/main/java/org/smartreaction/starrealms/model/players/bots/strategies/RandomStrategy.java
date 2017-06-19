package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class RandomStrategy implements BotStrategy {
    @Override
    public int getBuyCardScore(Card card, Player player) {
        return 1;
    }

    public int getBuyScoreIncreaseForAverageTradeRowCost(int averageTradeRowCost, Card card, int deckNumber) {
        return 0;
    }
}
