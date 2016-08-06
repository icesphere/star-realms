package org.smartreaction.starrealms.model.cards.ships.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class AssaultPod extends Ship implements AlliableCard {
    public AssaultPod() {
        name = "Assault Pod";
        addFaction(Faction.BLOB);
        addFaction(Faction.STAR_EMPIRE);
        cost = 2;
        set = CardSet.UNITED_SHIPS_STATIONS_AND_PODS;
        text = "Add 3 Combat; Ally: Draw a card";
        autoAlly = true;
        allFactionsAlliedTogether = true;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(3);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }
}
