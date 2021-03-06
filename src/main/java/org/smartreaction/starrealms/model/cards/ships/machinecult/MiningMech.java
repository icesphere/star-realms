package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class MiningMech extends Ship implements AlliableCard
{
    public MiningMech()
    {
        name = "Mining Mech";
        addFaction(Faction.MACHINE_CULT);
        cost = 4;
        set = CardSet.COLONY_WARS;
        text = "Add 3 Trade; You may scrap a card in your hand or discard pile; Ally: Add 3 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
        player.optionallyScrapCardFromHandOrDiscard();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(3);
    }

    @Override
    public int getTradeWhenPlayed() {
        return 3;
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
