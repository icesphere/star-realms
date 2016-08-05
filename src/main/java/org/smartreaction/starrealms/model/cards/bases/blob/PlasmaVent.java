package org.smartreaction.starrealms.model.cards.bases.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class PlasmaVent extends Base
{
    public PlasmaVent()
    {
        name = "Plasma Vent";
        addFaction(Faction.BLOB);
        cost = 6;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Add 4 Combat. When you acquire this card, if you've played a Blob card this turn, you may put this card directly into your hand.";
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(4);
    }
}
