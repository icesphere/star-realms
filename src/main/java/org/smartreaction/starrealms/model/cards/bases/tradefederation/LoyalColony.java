package org.smartreaction.starrealms.model.cards.bases.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class LoyalColony extends Base
{
    public LoyalColony()
    {
        name = "Loyal Colony";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 7;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Add 3 Trade. Add 3 Combat. Add 3 Authority.";
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(3);
        player.addCombat(3);
        player.addAuthority(3);
    }
}
