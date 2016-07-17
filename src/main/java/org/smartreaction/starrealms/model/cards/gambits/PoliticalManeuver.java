package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.players.Player;

public class PoliticalManeuver extends Gambit implements ScrappableGambit {
    public PoliticalManeuver() {
        name = "Political Maneuver";
    }

    @Override
    public void scrapGambit(Player player) {
        player.addTrade(2);
    }
}
