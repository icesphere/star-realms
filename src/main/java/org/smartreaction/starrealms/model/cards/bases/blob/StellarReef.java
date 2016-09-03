package org.smartreaction.starrealms.model.cards.bases.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class StellarReef extends Base implements ScrappableCard
{
    public StellarReef()
    {
        name = "Stellar Reef";
        addFaction(Faction.BLOB);
        cost = 2;
        set = CardSet.COLONY_WARS;
        shield = 3;
        text = "Add 1 Trade. Scrap: Add 3 Combat";
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(1);
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.addCombat(3);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 3;
    }

    @Override
    public int getTradeWhenPlayed() {
        return 1;
    }
}
