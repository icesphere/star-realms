package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class Supernova extends Event {
    public Supernova() {
        name = "Supernova";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player loses 5 Authority; Scrap all cards in the trade row";
    }

    @Override
    public void handleEvent(Player player) {
        player.reduceAuthority(5);
        player.getOpponent().reduceAuthority(5);
        player.getGame().scrapAllCardsFromTradeRow();
    }
}
