package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.players.Player;

public class UnlikelyAlliance extends Gambit implements ScrappableGambit {
    public UnlikelyAlliance() {
        name = "Unlikely Alliance";
    }

    @Override
    public void scrapGambit(Player player) {
        player.drawCards(2);
    }
}
