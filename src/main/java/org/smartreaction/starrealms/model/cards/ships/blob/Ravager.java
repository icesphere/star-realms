package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Ravager extends Ship
{
    public Ravager()
    {
        name = "Ravager";
        faction = Faction.BLOB;
        cost = 3;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat; You may scrap up to two cards that are currently in the trade row";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(6);
        player.optionallyScrapCardsInTradeRow(2);
    }
}
