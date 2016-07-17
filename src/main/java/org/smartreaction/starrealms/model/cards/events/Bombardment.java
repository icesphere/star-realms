package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class Bombardment extends Event {
    public Bombardment() {
        name = "Bombardment";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player either destroys a base they control or loses 6 Authority";
    }

    @Override
    public void handleEvent(Player player) {
        player.handleBombardment();
        player.getOpponent().handleBombardment();
    }
}
