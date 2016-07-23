package org.smartreaction.starrealms.model.cards.bases.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class TheHive extends Base implements AlliableCard
{
    public TheHive()
    {
        name = "The Hive";
        faction = Faction.BLOB;
        cost = 5;
        set = CardSet.CORE;
        shield = 5;
        text = "Add 3 Combat; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(3);
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
