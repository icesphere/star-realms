package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.players.Player;

public class SmugglingRun extends Gambit implements ScrappableGambit {
    public SmugglingRun() {
        name = "Smuggling Run";
    }

    @Override
    public void scrapGambit(Player player) {
        player.acquireFreeCard(4);
    }
}
