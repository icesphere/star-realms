package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class HeavyCruiser extends Ship implements AlliableCard
{
    public HeavyCruiser()
    {
        name = "Heavy Cruiser";
        addFaction(Faction.STAR_EMPIRE);
        cost = 5;
        set = CardSet.COLONY_WARS;
        text = "Add 4 Combat; Draw a card; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(4);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.drawCard();
    }
}
