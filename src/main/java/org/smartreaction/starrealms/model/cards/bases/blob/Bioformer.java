package org.smartreaction.starrealms.model.cards.bases.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class Bioformer extends Base implements ScrappableCard
{
    public Bioformer()
    {
        name = "Bioformer";
        faction = Faction.BLOB;
        cost = 4;
        set = CardSet.COLONY_WARS;
        shield = 4;
        text = "Add 3 Combat; Scrap: Add 3 Trade";
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(3);
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
