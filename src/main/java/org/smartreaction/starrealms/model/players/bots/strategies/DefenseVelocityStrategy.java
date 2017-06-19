package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class DefenseVelocityStrategy implements BotStrategy {
    DefenseStrategy defenseStrategy = new DefenseStrategy();
    VelocityStrategy velocityStrategy = new VelocityStrategy();

    @Override
    public int getBuyCardScore(Card card, Player player) {
        int defenseScore = defenseStrategy.getBuyCardScore(card, player);
        int velocityScore = velocityStrategy.getBuyCardScore(card, player);

        int total = defenseScore + velocityScore;

        if (total == 0) {
            return 0;
        }

        return total / 2;
    }

    public int getBuyScoreIncreaseForAverageTradeRowCost(int averageTradeRowCost, Card card, int deckNumber) {
        int defenseStrategyScore = defenseStrategy.getBuyScoreIncreaseForAverageTradeRowCost(averageTradeRowCost, card, deckNumber);
        int velocityStrategyScore = velocityStrategy.getBuyScoreIncreaseForAverageTradeRowCost(averageTradeRowCost, card, deckNumber);

        int total = defenseStrategyScore + velocityStrategyScore;

        if (total == 0) {
            return 0;
        }

        return total / 2;
    }
}
