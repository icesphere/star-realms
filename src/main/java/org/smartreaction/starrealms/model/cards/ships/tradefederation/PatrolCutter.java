package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class PatrolCutter extends Ship implements AlliableCard
{
    public PatrolCutter()
    {
        name = "Patrol Cutter";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 3;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Trade; Add 3 Combat; Ally: Add 4 Authority";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addTrade(2);
        player.addCombat(3);
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.addAuthority(4);
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }
}
