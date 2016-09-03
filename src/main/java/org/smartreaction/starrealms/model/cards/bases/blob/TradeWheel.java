package org.smartreaction.starrealms.model.cards.bases.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class TradeWheel extends Base implements AlliableCard
{
    public TradeWheel()
    {
        name = "Trade Wheel";
        addFaction(Faction.BLOB);
        cost = 3;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        shield = 5;
        text = "Add 1 Trade; Ally: Add 2 Combat";
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(1);
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.addCombat(2);
    }

    @Override
    public int getTradeWhenPlayed() {
        return 1;
    }
}
