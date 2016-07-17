package org.smartreaction.starrealms.model.cards.heroes;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.players.Player;

public class CeoTorres extends Hero {
    public CeoTorres() {
        name = "CEO Torres";
        set = CardSet.CRISIS_HEROES;
        cost = 2;
        text = "Scrap: Add 7 Authority. Until end of turn, you may use all of your Trade Federation Ally abilities";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.TRADE_FEDERATION;
    }

    @Override
    public void cardScrapped(Player player) {
        player.addAuthority(7);
        player.tradeFederationAlliedUntilEndOfTurn();
    }

    @Override
    public int getAuthorityWhenScrapped() {
        return 7;
    }
}
