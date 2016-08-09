package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class SalvageOperation extends Gambit implements ScrappableCard {
    public SalvageOperation() {
        name = "Salvage Operation";
        text = "Scrap: Put target card from your discard pile on top of your deck";
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCardFromDiscardToTopOfDeck(null);
    }
}
