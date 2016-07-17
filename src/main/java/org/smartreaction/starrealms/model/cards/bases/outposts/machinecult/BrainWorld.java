package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class BrainWorld extends Outpost
{
    public BrainWorld()
    {
        name = "Brain World";
        faction = Faction.MACHINE_CULT;
        cost = 8;
        set = CardSet.CORE;
        shield = 6;
        text = "Scrap up to two cards from your hand and/or discard pile. Draw a card for each card scrapped this way.";
    }

    @Override
    public void baseUsed(Player player)
    {
        player.scrapToDrawCards(2);
    }
}
