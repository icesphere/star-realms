package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class WarningBeacon extends Outpost
{
    public WarningBeacon()
    {
        name = "Warning Beacon";
        addFaction(Faction.MACHINE_CULT);
        cost = 2;
        set = CardSet.COLONY_WARS;
        shield = 2;
        text = "Add 2 Combat. When you acquire this card, if you've played a Machine Cult card this turn, you may put this card directly into your hand.";
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(2);
    }
}
