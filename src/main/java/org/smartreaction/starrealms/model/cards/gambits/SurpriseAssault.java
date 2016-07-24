package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class SurpriseAssault extends Gambit implements ScrappableCard {
    public SurpriseAssault() {
        name = "Surprise Assault";
        text = "Scrap: Add 8 Combat";
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(8);
    }
}
