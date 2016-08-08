package org.smartreaction.starrealms.model.cards.heroes;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.players.Player;

public class SpecialOpsDirector extends Hero {
    public SpecialOpsDirector() {
        name = "Special Ops Director";
        set = CardSet.CRISIS_HEROES;
        cost = 1;
        text = "Scrap: Add 4 Authority. Until end of turn, you may use all of your Trade Federation Ally abilities";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.TRADE_FEDERATION;
    }

    @Override
    public void cardScrapped(Player player) {
        player.tradeFederationAlliedUntilEndOfTurn();
        player.addAuthority(4);
    }

    @Override
    public int getAuthorityWhenScrapped() {
        return 4;
    }
}
