package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Predator extends Ship implements AlliableCard
{
    public Predator()
    {
        name = "Predator";
        faction = Faction.BLOB;
        cost = 2;
        set = CardSet.COLONY_WARS;
        text = "Add 4 Combat; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(4);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.drawCard();
    }
}
