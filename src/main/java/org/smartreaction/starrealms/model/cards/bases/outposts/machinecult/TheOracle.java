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
        faction = Faction.MACHINE_CULT;
        cost = 4;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Scrap a card in your hand. Ally: Add 3 Combat.";
    }

    @Override
    public void baseUsed(Player player) {
        player.scrapCardFromHand(true);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(3);
    }
}
