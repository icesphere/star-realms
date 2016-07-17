package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class MechWorld extends Outpost
{
    public MechWorld()
    {
        name = "Mech World";
        faction = Faction.MACHINE_CULT;
        cost = 5;
        set = CardSet.CORE;
        shield = 6;
        text = "Mech World counts as an ally for all factions";
    }

    @Override
    public void cardPlayed(Player player) {
        player.allFactionsAllied();
    }

    @Override
    public void baseUsed(Player player) {

    }

    @Override
    public boolean isBlob() {
        return true;
    }

    @Override
    public boolean isTradeFederation() {
        return true;
    }

    @Override
    public boolean isMachineCult() {
        return true;
    }

    @Override
    public boolean isStarEmpire() {
        return true;
    }
}
