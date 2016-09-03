package org.smartreaction.starrealms.model.cards.ships;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class Explorer extends Ship implements ScrappableCard
{
    public Explorer()
    {
        name = "Explorer";
        cost = 2;
        set = CardSet.CORE;
        text = "Add 2 Trade; Scrap: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addTrade(2);
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.addCombat(2);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 2;
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }
}
