package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class UnlikelyAlliance extends Gambit implements ScrappableCard {
    public UnlikelyAlliance() {
        name = "Unlikely Alliance";
        text = "Draw 2 cards";
    }

    @Override
    public void cardScrapped(Player player) {
        player.drawCards(2);
    }
}
