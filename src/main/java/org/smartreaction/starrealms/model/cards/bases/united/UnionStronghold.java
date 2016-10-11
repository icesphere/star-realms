package org.smartreaction.starrealms.model.cards.bases.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class UnionStronghold extends Base implements AlliableCard {
    public UnionStronghold() {
        name = "Union Stronghold";
        addFaction(Faction.BLOB);
        addFaction(Faction.STAR_EMPIRE);
        cost = 5;
        set = CardSet.UNITED_ASSAULT;
        shield = 5;
        text = "Add 3 Combat; Ally Blob: Scrap a card in the trade row; Ally Star Empire: Target opponent discards a card";
        autoAlly = true;
        addAutoAllyExcludedFaction(Faction.BLOB);
        autoUse = true;
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
    public void cardAllied(Player player, Faction faction) {
        if (faction == Faction.BLOB) {
            player.optionallyScrapCardsInTradeRow(1);
        } else if (faction == Faction.STAR_EMPIRE) {
            player.opponentDiscardsCard();
        }
    }
}
