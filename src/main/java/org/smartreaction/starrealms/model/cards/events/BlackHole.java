package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class BlackHole extends Event {
    public BlackHole() {
        name = "Black Hole";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player may discard up to two cards. For each card less than two that a player discards, that player loses 4 Authority";
    }

    @Override
    public void handleEvent(Player player) {
        player.handleBlackHole();
        player.getOpponent().handleBlackHole();
    }
}
