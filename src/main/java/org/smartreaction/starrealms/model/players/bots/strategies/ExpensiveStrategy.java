package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class ExpensiveStrategy implements BotStrategy {
    @Override
    public int getBuyCardScore(Card card, Player player) {
        return card.getCost() * card.getCost();
    }
}
