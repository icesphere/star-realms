package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;
import org.smartreaction.starrealms.model.players.bots.strategies.VelocityStrategy;

public class VelocityBot extends BotPlayer {
    BotStrategy strategy = new VelocityStrategy();

    @Override
    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
