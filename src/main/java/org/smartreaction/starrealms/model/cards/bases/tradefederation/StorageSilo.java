package org.smartreaction.starrealms.model.cards.bases.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class StorageSilo extends Base implements AlliableCard
{
    public StorageSilo()
    {
        name = "Storage Silo";
        faction = Faction.TRADE_FEDERATION;
        cost = 2;
        set = CardSet.COLONY_WARS;
        shield = 3;
        text = "Add 2 Authority; Ally: Add 2 Trade";
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addAuthority(2);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addTrade(2);
    }
}
