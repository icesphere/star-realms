package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Swarmer extends Ship implements AlliableCard
{
    public Swarmer()
    {
        name = "Swarmer";
        addFaction(Faction.BLOB);
        cost = 1;
        set = CardSet.COLONY_WARS;
        text = "Add 3 Combat; You may scrap a card in the trade row; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(3);
        player.optionalScrapCardInTradeRow();
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.addCombat(2);
    }
}
