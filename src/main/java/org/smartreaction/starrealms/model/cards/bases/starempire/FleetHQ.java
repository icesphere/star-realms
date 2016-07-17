package org.smartreaction.starrealms.model.cards.bases.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class FleetHQ extends Base
{
    public FleetHQ()
    {
        name = "Fleet HQ";
        faction = Faction.STAR_EMPIRE;
        cost = 8;
        set = CardSet.CORE;
        shield = 8;
        text = "All of your ships get \"Add 1 Combat\"";
    }

    @Override
    public void cardPlayed(Player player) {
        player.allShipsGet1Combat();
    }

    @Override
    public void baseUsed(Player player) {

    }
}
