package org.smartreaction.starrealms.model.cards.bases.outposts.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class LookoutPost extends Outpost {
    public LookoutPost() {
        name = "Lookout Post";
        addFaction(Faction.TRADE_FEDERATION);
        addFaction(Faction.MACHINE_CULT);
        cost = 7;
        set = CardSet.UNITED_ASSAULT;
        shield = 6;
        text = "Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        // do nothing
    }

    @Override
    public void baseUsed(Player player) {
        player.drawCard();
    }
}
