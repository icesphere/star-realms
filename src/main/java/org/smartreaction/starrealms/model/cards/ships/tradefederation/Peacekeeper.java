package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Peacekeeper extends Ship implements AlliableCard
{
    public Peacekeeper()
    {
        name = "Peacekeeper";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 6;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat; Add 6 Authority; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.addAuthority(6);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }
}
