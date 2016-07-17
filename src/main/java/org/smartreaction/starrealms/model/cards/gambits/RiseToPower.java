package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.players.Player;

public class RiseToPower extends Gambit implements ScrappableGambit {
    public RiseToPower() {
        name = "Rise to Power";
    }

    @Override
    public void scrapGambit(Player player) {
        player.addAuthority(8);
        player.drawCard();
    }
}
