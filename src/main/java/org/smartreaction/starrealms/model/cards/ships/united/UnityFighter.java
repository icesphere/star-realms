package org.smartreaction.starrealms.model.cards.ships.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class UnityFighter extends Ship implements ScrappableCard {
    public UnityFighter() {
        name = "Unity Fighter";
        addFaction(Faction.BLOB);
        addFaction(Faction.MACHINE_CULT);
        cost = 1;
        set = CardSet.UNITED_SHIPS_STATIONS_AND_PODS;
        text = "Add 3 Combat; You may scrap a card in the trade row; Scrap: You may scrap a card in your hand or discard pile";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(3);
        player.optionallyScrapCardsInTradeRow(1);
    }

    @Override
    public void cardScrapped(Player player) {
        player.optionallyScrapCardFromHandOrDiscard();
    }
}
