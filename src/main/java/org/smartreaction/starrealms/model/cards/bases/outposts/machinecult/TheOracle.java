package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class TheOracle extends Outpost implements AlliableCard
{
    public TheOracle()
    {
        name = "The Oracle";
        addFaction(Faction.MACHINE_CULT);
        cost = 4;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Scrap a card in your hand. Ally: Add 3 Combat.";
    }

    @Override
    public void baseUsed(Player player) {
        player.scrapCardFromHand(false);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(3);
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
