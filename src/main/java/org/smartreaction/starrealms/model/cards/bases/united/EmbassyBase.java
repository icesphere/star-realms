package org.smartreaction.starrealms.model.cards.bases.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class EmbassyBase extends Base {
    public EmbassyBase() {
        name = "Embassy Base";
        addFaction(Faction.STAR_EMPIRE);
        addFaction(Faction.TRADE_FEDERATION);
        cost = 8;
        set = CardSet.UNITED_ASSAULT;
        shield = 6;
        text = "Draw two cards then discard a card";
    }

    @Override
    public void cardPlayed(Player player) {
        // do nothing
    }

    @Override
    public void baseUsed(Player player) {
        player.drawCards(2);
        player.discardCardFromHand();
    }
}
