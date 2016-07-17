package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.players.Player;

public class BoldRaid extends Gambit implements ScrappableGambit {
    public BoldRaid() {
        name = "Bold Raid";
    }

    @Override
    public void scrapGambit(Player player) {
        player.destroyTargetBase();
        player.drawCard();
    }
}
