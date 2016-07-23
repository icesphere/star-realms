package org.smartreaction.starrealms.model.cards.bases.blob;


import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class BlobWheel extends Base implements ScrappableCard
{
    public BlobWheel()
    {
        name = "Blob Wheel";
        faction = Faction.BLOB;
        cost = 3;
        set = CardSet.CORE;
        shield = 5;
        text = "Add 1 Combat; Scrap: Add 3 Trade";
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(1);
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.addTrade(3);
    }

    @Override
    public int getTradeWhenScrapped() {
        return 3;
    }
}
