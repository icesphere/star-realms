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
        addFaction(Faction.STAR_EMPIRE);
        cost = 8;
        set = CardSet.CORE;
        shield = 8;
        text = "All of your ships get \"Add 1 Combat\"";
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.setAllShipsAddOneCombat(true);
    }

    @Override
    public void removedFromPlay(Player player) {
        player.setAllShipsAddOneCombat(false);
    }
}
