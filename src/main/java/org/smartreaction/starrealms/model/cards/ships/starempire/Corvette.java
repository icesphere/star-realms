package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Corvette extends Ship implements AlliableCard
{
    public Corvette()
    {
        name = "Corvette";
        addFaction(Faction.STAR_EMPIRE);
        cost = 2;
        set = CardSet.CORE;
        text = "Add 1 Combat; Draw a card; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(1);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.addCombat(2);
    }
}
