package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class RiseToPower extends Gambit implements ScrappableCard {
    public RiseToPower() {
        name = "Rise to Power";
    }

    @Override
    public void cardScrapped(Player player) {
        player.addAuthority(8);
        player.drawCard();
    }
}
