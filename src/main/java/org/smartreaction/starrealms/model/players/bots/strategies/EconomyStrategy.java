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
        } else if (card.getTradeWhenPlayed() > 0) {
            score += 10 * card.getTradeWhenPlayed();
        }

        return score;
    }
}
