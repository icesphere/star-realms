package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class BattleStation extends Outpost implements ScrappableCard
{
    public BattleStation()
    {
        name = "Battle Station";
        addFaction(Faction.MACHINE_CULT);
        cost = 3;
        set = CardSet.CORE;
        shield = 5;
        text = "Scrap: Add 5 Combat";
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.addCombat(5);
    }

    @Override
    public void baseUsed(Player player) {

    }

    @Override
    public int getCombatWhenScrapped() {
        return 5;
    }
}
