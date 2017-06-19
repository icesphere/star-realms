package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class EconomyStrategy implements BotStrategy {
    VelocityStrategy velocityStrategy = new VelocityStrategy();

    @Override
    public int getBuyCardScore(Card card, Player player) {
        int score = velocityStrategy.getBuyCardScore(card, player);

        if (card.getCost() >= 6) {
            score += card.getCost();
        } else if (card.getTradeWhenPlayed() > 0 && player.getTrade() < 6) {
            score += 10 * card.getTradeWhenPlayed();
        }

        return score;
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
