package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.BotPlayer;

public class RandomBot extends BotPlayer {

    @Override
    public int getBuyCardScore(Card card) {
        return 1;
    }
}
