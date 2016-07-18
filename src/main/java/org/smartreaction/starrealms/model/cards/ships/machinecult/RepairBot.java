package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class RepairBot extends Ship implements ScrappableCard
{
    public RepairBot()
    {
        name = "Repair Bot";
        faction = Faction.MACHINE_CULT;
        cost = 2;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Trade; You may scrap a card in your discard pile; Scrap: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
        player.optionallyScrapCardsFromDiscard(1);
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(2);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 2;
    }
}
