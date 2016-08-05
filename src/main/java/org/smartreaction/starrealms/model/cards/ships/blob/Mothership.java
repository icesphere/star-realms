package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Mothership extends Ship implements AlliableCard
{
    public Mothership()
    {
        name = "Mothership";
        addFaction(Faction.BLOB);
        cost = 7;
        set = CardSet.CORE;
        text = "Add 6 Combat; Draw a card; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }
}
