package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class MachineBase extends Outpost
{
    public MachineBase()
    {
        name = "Machine Base";
        addFaction(Faction.MACHINE_CULT);
        cost = 7;
        set = CardSet.CORE;
        shield = 6;
        text = "Draw a card, then scrap a card from your hand";
    }

    @Override
    public void baseUsed(Player player) {
        player.drawCard();
        player.scrapCardFromHand(false);
    }
}
