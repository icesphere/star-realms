package org.smartreaction.starrealms.model.cards.bases.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class CentralStation extends Base
{
    public CentralStation()
    {
        name = "Central Station";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 4;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Add 2 Trade. If you have three or more bases in play (including this one), gain 4 Authority and draw a card.";
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(2);
        if (player.getBases().size() >= 3) {
            player.addAuthority(4);
            player.drawCard();
        }
    }
}
