package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.bots.strategies.AttackVelocityStrategy;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;

public class HareBot extends BotPlayer {
    BotStrategy strategy = new AttackVelocityStrategy();

    @Override
    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
