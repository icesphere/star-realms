package org.smartreaction.starrealms.model.cards.ships.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class BlobBot extends Ship implements AlliableCard {
    public BlobBot() {
        name = "Blob Bot";
        addFaction(Faction.BLOB);
        addFaction(Faction.MACHINE_CULT);
        cost = 3;
        set = CardSet.UNITED_ASSAULT;
        text = "Add 5 Combat; Ally Blob: Add 2 Trade; Ally Machine Cult: Scrap a card in your hand or discard pile";
        autoAlly = true;
        addAutoAllyExcludedFaction(Faction.MACHINE_CULT);
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        if (faction == Faction.BLOB) {
            player.addTrade(2);
        } else if (faction == Faction.MACHINE_CULT) {
            player.optionallyScrapCardsFromHandOrDiscard(1);
        }
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
