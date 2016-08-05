package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class MegaMech extends Ship implements AlliableCard
{
    public MegaMech()
    {
        name = "Mega Mech";
        addFaction(Faction.MACHINE_CULT);
        cost = 5;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 6 Combat; You may return target base from play to its owner's hand; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.returnTargetBaseToHand();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }
}
