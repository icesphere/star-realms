package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class BlobDestroyer extends Ship implements AlliableCard
{
    public BlobDestroyer()
    {
        name = "Blob Destroyer";
        faction = Faction.BLOB;
        cost = 4;
        set = CardSet.CORE;
        text = "Add 6 Combat; Ally: You may destroy target base and/or scrap a card in the trade row";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(6);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.destroyTargetBase();
        player.optionalScrapCardInTradeRow();
    }
}
