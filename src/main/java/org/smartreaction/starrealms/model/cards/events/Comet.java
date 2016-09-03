package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class Comet extends Event {
    public Comet() {
        name = "Comet";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player may scrap up to two cards in their hand or discard pile";
    }

    @Override
    public void handleEvent(Player player) {
        player.optionallyScrapCardsFromHandOrDiscard(2);
        player.getOpponent().optionallyScrapCardsFromHandOrDiscard(2);
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
