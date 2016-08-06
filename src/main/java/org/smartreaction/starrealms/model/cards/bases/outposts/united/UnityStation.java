package org.smartreaction.starrealms.model.cards.bases.outposts.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class UnityStation extends Outpost implements AlliableCard {
    public UnityStation() {
        name = "Unity Station";
        addFaction(Faction.BLOB);
        addFaction(Faction.MACHINE_CULT);
        cost = 7;
        set = CardSet.UNITED_SHIPS_STATIONS_AND_PODS;
        shield = 6;
        text = "Scrap a card in your hand or discard pile; You may scrap a card in the trade row; Ally: Add 4 Combat";
        allFactionsAlliedTogether = true;
        autoAlly = true;
    }

    @Override
    public void cardPlayed(Player player) {
        //do nothing
    }

    @Override
    public void baseUsed(Player player) {
        player.optionallyScrapCardFromHandOrDiscard();
        player.optionalScrapCardInTradeRow();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(4);
    }
}
