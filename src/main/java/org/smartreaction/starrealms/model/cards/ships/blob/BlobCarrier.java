package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class BlobCarrier extends Ship implements AlliableCard
{
    public BlobCarrier()
    {
        name = "Blob Carrier";
        addFaction(Faction.BLOB);
        cost = 6;
        set = CardSet.CORE;
        text = "Add 7 Combat; Ally: Acquire any ship without paying its cost and put it on top of your deck";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(7);
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.acquireFreeShipAndPutOnTopOfDeck();
    }
}
