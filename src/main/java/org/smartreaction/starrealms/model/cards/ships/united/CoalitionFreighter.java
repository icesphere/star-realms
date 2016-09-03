package org.smartreaction.starrealms.model.cards.ships.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class CoalitionFreighter extends Ship implements AlliableCard {
    public CoalitionFreighter() {
        name = "Coalition Freighter";
        addFaction(Faction.TRADE_FEDERATION);
        addFaction(Faction.MACHINE_CULT);
        cost = 4;
        set = CardSet.UNITED_SHIPS_STATIONS_AND_PODS;
        text = "Add 3 Trade; Ally Trade Federation: Put the next ship you acquire this turn on top of your deck; Ally Machine Cult: Scrap a card in your hand or discard pile";
        autoAlly = true;
        addAutoAllyExcludedFaction(Faction.MACHINE_CULT);
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        if (faction == Faction.TRADE_FEDERATION) {
            player.nextShipToTopOfDeck();
        } else if (faction == Faction.MACHINE_CULT) {
            player.optionallyScrapCardFromHandOrDiscard();
        }
    }

    @Override
    public int getTradeWhenPlayed() {
        return 3;
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
