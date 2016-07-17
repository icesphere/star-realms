package org.smartreaction.starrealms.model;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public interface BotStrategy {
    public int getBuyCardScore(Card card, Player player);
}
