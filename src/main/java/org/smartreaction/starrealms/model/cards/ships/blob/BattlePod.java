package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class BattlePod extends Ship implements AlliableCard
{
    public BattlePod()
    {
        name = "Battle Pod";
        addFaction(Faction.BLOB);
        cost = 2;
        set = CardSet.CORE;
        text = "Add 4 Combat; You may scrap a card in the trade row; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(4);
        player.optionalScrapCardInTradeRow();
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.addCombat(2);
    }
}
