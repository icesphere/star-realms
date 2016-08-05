package org.smartreaction.starrealms.model.cards.ships.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class AllianceTransport extends Ship implements AlliableCard {
    public AllianceTransport() {
        name = "AllianceTransport";
        addFaction(Faction.STAR_EMPIRE);
        addFaction(Faction.TRADE_FEDERATION);
        cost = 2;
        set = CardSet.UNITED_VARIOUS;
        text = "Add 2 Trade; Ally Star Empire: Target opponent discards a card; Ally Trade Federation: Add 4 Authority";
        autoAlly = true;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        if (faction == Faction.STAR_EMPIRE) {
            player.opponentDiscardsCard();
        } else if (faction == Faction.TRADE_FEDERATION) {
            player.addAuthority(4);
        }
    }
}
