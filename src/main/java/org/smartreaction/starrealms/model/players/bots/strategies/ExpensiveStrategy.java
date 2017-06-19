package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class ExpensiveStrategy implements BotStrategy {
    @Override
    public int getBuyCardScore(Card card, Player player) {
        return card.getCost() * card.getCost();
    }

    public int getBuyScoreIncreaseForAverageTradeRowCost(int averageTradeRowCost, Card card, int deckNumber) {
        if (averageTradeRowCost >= 5 && deckNumber <= 2) {
            if (card.getTradeWhenPlayed() >= 2 || card.getTradeWhenScrapped() >= 3) {
                if (averageTradeRowCost >= 7) {
                    return 20;
                } else if (averageTradeRowCost >= 6) {
                    return 15;
                } else {
                    return 10;
                }
            }
        }

        return 0;
    }
}
