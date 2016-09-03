package org.smartreaction.starrealms.model.cards.heroes.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.players.Player;

public class ChairmanHaygan extends Hero {
    public ChairmanHaygan() {
        name = "Chairman Haygan";
        set = CardSet.UNITED_HEROES;
        cost = 3;
        text = "Buy: Until end of turn, you may use all of your Trade Federation Ally abilities. Add 4 Authority; Scrap: Until end of turn, you may use all of your Trade Federation Ally abilities. Add 4 Authority.";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.TRADE_FEDERATION;
    }

    @Override
    public void heroAcquired(Player player) {
        player.tradeFederationAlliedUntilEndOfTurn();
        player.addAuthority(4);
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
