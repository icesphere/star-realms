package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class Junkyard extends Outpost
{
    public Junkyard()
    {
        name = "Junkyard";
        addFaction(Faction.MACHINE_CULT);
        cost = 6;
        set = CardSet.CORE;
        shield = 5;
        text = "Scrap a card in your hand or discard pile";
    }

    @Override
    public void baseUsed(Player player) {
        player.optionallyScrapCardFromHandOrDiscard();
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
