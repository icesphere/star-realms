package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class Quasar extends Event {
    public Quasar() {
        name = "Quasar";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player draws two cards";
    }

    @Override
    public void handleEvent(Player player) {
        player.drawCards(2);
        player.getOpponent().drawCards(2);
    }
}
