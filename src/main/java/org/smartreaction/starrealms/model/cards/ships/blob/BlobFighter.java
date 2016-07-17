package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class BlobFighter extends Ship implements AlliableCard
{
    public BlobFighter()
    {
        name = "Blob Fighter";
        faction = Faction.BLOB;
        cost = 1;
        set = CardSet.CORE;
        text = "Add 3 Combat; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(3);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.drawCard();
    }
}
