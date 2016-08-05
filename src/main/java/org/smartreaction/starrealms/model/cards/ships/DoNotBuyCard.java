package org.smartreaction.starrealms.model.cards.ships;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class DoNotBuyCard extends Ship
{
    public DoNotBuyCard()
    {
        name = "Do Not Buy Card";
        cost = 0;
        set = CardSet.CORE;
        text = "This is a placeholder to show simulation results of not buying card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(1);
    }
}
