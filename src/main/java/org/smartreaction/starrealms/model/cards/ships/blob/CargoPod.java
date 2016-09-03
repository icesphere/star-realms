package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class CargoPod extends Ship implements AlliableCard, ScrappableCard
{
    public CargoPod()
    {
        name = "Cargo Pod";
        addFaction(Faction.BLOB);
        cost = 3;
        set = CardSet.COLONY_WARS;
        text = "Add 3 Trade; Ally: Add 3 Combat; Scrap: Add 3 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(3);
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(3);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 3;
    }

    @Override
    public int getTradeWhenPlayed() {
        return 3;
    }
}
