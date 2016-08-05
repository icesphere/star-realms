package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class MissileMech extends Ship implements AlliableCard
{
    public MissileMech()
    {
        name = "Missile Mech";
        addFaction(Faction.MACHINE_CULT);
        cost = 6;
        set = CardSet.CORE;
        text = "Add 6 Combat; You may destroy target base; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.destroyTargetBase();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }
}
