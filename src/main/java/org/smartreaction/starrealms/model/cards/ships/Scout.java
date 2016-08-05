package org.smartreaction.starrealms.model.cards.ships;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class Scout extends Ship
{
    public Scout()
    {
        name = "Scout";
        cost = 0;
        set = CardSet.CORE;
        text = "Add 1 Trade";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(1);
    }
}
