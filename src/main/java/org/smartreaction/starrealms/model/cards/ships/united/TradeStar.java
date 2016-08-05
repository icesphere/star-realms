package org.smartreaction.starrealms.model.cards.ships.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class TradeStar extends Ship implements ScrappableCard {
    public TradeStar() {
        name = "Trade Star";
        addFaction(Faction.BLOB);
        addFaction(Faction.STAR_EMPIRE);
        cost = 1;
        set = CardSet.UNITED_VARIOUS;
        text = "Add 2 Trade; Scrap: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(2);
    }
}
