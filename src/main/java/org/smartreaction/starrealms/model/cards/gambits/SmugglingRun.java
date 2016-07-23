package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class SmugglingRun extends Gambit implements ScrappableCard {
    public SmugglingRun() {
        name = "Smuggling Run";
    }

    @Override
    public void cardScrapped(Player player) {
        player.acquireFreeCard(4);
    }
}
