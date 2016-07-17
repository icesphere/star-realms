package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class TradeMission extends Event {
    public TradeMission() {
        name = "Trade Mission";
        set = CardSet.CRISIS_EVENTS;
        text = "The player currently taking their turn gains 4 Trade and may put the next ship they acquire this turn on top of their deck. Each other player draws two cards.";
    }

    @Override
    public void handleEvent(Player player) {
        player.addAuthority(4);
        player.nextShipToTopOfDeck();
        player.getOpponent().drawCards(2);
    }
}
