package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.bots.strategies.AttackStrategy;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;

public class AttackBot extends BotPlayer {
    BotStrategy strategy = new AttackStrategy();

    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
