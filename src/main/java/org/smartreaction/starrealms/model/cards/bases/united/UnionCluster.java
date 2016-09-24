package org.smartreaction.starrealms.model.cards.bases.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class UnionCluster extends Base implements AlliableCard {
    public UnionCluster() {
        name = "Union Cluster";
        addFaction(Faction.BLOB);
        addFaction(Faction.STAR_EMPIRE);
        cost = 8;
        set = CardSet.UNITED_COMMAND;
        shield = 8;
        text = "Add 4 Combat; Ally: Draw a card";
        allFactionsAlliedTogether = true;
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(4);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }
}
