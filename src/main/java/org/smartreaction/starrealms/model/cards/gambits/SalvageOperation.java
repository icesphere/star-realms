package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.players.Player;

public class SalvageOperation extends Gambit implements ScrappableGambit {
    public SalvageOperation() {
        name = "Salvage Operation";
    }

    @Override
    public void scrapGambit(Player player) {
        player.addCardFromDiscardToTopOfDeck();
    }
}
