package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class SurpriseAssault extends Gambit implements ScrappableCard {
    public SurpriseAssault() {
        name = "Surprise Assault";
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(8);
    }
}
