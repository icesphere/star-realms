package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.players.Player;

public class SurpriseAssault extends Gambit implements ScrappableGambit {
    public SurpriseAssault() {
        name = "Surprise Assault";
    }

    @Override
    public void scrapGambit(Player player) {
        player.addCombat(8);
    }
}
