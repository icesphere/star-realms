package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class SupplyBot extends Ship implements AlliableCard
{
    public SupplyBot()
    {
        name = "Supply Bot";
        addFaction(Faction.MACHINE_CULT);
        cost = 3;
        set = CardSet.CORE;
        text = "Add 2 Trade; You may scrap a card in your hand or discard pile; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
        player.optionallyScrapCardFromHandOrDiscard();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(2);
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
