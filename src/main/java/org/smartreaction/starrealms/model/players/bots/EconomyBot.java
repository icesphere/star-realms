package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;
import org.smartreaction.starrealms.model.players.bots.strategies.EconomyStrategy;

public class EconomyBot extends BotPlayer {
    BotStrategy strategy = new EconomyStrategy();

    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
