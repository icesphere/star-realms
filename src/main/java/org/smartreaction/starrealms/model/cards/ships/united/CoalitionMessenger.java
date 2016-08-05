package org.smartreaction.starrealms.model.cards.ships.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.CardFromDiscardToTopOfDeck;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class CoalitionMessenger extends Ship implements AlliableCard {
    public CoalitionMessenger() {
        name = "Coalition Messenger";
        addFaction(Faction.MACHINE_CULT);
        addFaction(Faction.TRADE_FEDERATION);
        cost = 3;
        set = CardSet.UNITED_VARIOUS;
        text = "Add 2 Trade; Ally: Choose a card of cost 5 or less in your discard pile. Put it on top of your deck.";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addAction(new CardFromDiscardToTopOfDeck("Choose a card from your discard pile of cost 5 or less to put on top of your deck", 5));
    }
}
