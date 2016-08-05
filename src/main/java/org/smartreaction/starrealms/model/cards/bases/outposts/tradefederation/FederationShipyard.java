package org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class FederationShipyard extends Outpost implements AlliableCard
{
    public FederationShipyard()
    {
        name = "Federation Shipyard";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 6;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Add 2 Trade. Ally: Put the next ship or base you acquire this turn on top of your deck.";
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player)
    {
        player.addTrade(2);
        player.nextShipOrBaseToTopOfDeck();
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.addCombat(2);
    }
}
