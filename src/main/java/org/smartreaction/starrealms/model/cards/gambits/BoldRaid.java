package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class BoldRaid extends Gambit implements ScrappableCard {
    public BoldRaid() {
        name = "Bold Raid";
    }

    @Override
    public void cardScrapped(Player player) {
        player.destroyTargetBase();
        player.drawCard();
    }
}
