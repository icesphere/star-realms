package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class PoliticalManeuver extends Gambit implements ScrappableCard {
    public PoliticalManeuver() {
        name = "Political Maneuver";
        text = "Scrap: Add 2 Trade";
    }

    @Override
    public void cardScrapped(Player player) {
        player.addTrade(2);
    }
}
