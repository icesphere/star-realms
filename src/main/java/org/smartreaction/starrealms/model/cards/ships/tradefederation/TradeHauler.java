package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class TradeHauler extends Ship implements AlliableCard
{
    public TradeHauler()
    {
        name = "Trade Hauler";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 2;
        set = CardSet.COLONY_WARS;
        text = "Add 3 Trade; Ally: Add 3 Authority";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addAuthority(3);
    }
}
