package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class SolarSkiff extends Ship implements AlliableCard
{
    public SolarSkiff()
    {
        name = "Solar Skiff";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 1;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Trade; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }
}
