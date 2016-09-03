package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class AttackVelocityStrategy implements BotStrategy {
    AttackStrategy attackStrategy = new AttackStrategy();
    VelocityStrategy velocityStrategy = new VelocityStrategy();

    @Override
    public int getBuyCardScore(Card card, Player player) {
        int attackScore = attackStrategy.getBuyCardScore(card, player);
        int velocityScore = velocityStrategy.getBuyCardScore(card, player);

        int total = attackScore + velocityScore;

        if (total == 0) {
            return 0;
        }

        return total / 2;
    }
}
