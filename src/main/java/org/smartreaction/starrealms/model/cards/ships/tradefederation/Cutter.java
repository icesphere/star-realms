package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Cutter extends Ship implements AlliableCard
{
    public Cutter()
    {
        name = "Cutter";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 2;
        set = CardSet.CORE;
        text = "Add 4 Authority; Add 2 Trade; Ally: Add 4 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addAuthority(4);
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.addCombat(4);
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }
}
