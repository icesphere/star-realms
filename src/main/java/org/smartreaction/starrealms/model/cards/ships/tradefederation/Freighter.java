package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Freighter extends Ship implements AlliableCard
{
    public Freighter()
    {
        name = "Freighter";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 4;
        set = CardSet.CORE;
        text = "Add 4 Trade; Ally: You may put the next ship you acquire this turn on top of your deck";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(4);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.nextShipToTopOfDeck();
    }
}
