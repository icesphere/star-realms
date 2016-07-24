package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.actions.CardFromDiscardToTopOfDeck;
import org.smartreaction.starrealms.model.players.Player;

public class SalvageOperation extends Gambit implements ScrappableCard {
    public SalvageOperation() {
        name = "Salvage Operation";
    }

    @Override
    public void cardScrapped(Player player) {
        player.addAction(new CardFromDiscardToTopOfDeck("Add a card from your discard to the top of your deck"));
    }
}
