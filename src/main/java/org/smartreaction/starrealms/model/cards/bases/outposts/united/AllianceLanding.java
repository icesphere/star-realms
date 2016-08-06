package org.smartreaction.starrealms.model.cards.bases.outposts.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class AllianceLanding extends Outpost implements AlliableCard {
    public AllianceLanding() {
        name = "Alliance Landing";
        addFaction(Faction.TRADE_FEDERATION);
        addFaction(Faction.STAR_EMPIRE);
        cost = 5;
        set = CardSet.UNITED_SHIPS_STATIONS_AND_PODS;
        shield = 5;
        text = "Add 2 Trade; Ally: Add 2 Combat";
        allFactionsAlliedTogether = true;
        autoAlly = true;
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(2);
    }
}
