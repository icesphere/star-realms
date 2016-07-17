package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class WarpJump extends Event {
    public WarpJump() {
        name = "Warp Jump";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player draws three cards, then puts two of those cards back on top of their deck in any order";
    }

    @Override
    public void handleEvent(Player player) {
        player.drawCardsAndPutSomeBackOnTop(3, 2);
        player.getOpponent().drawCardsAndPutSomeBackOnTop(3, 2);
    }
}
