package org.smartreaction.starrealms.model.cards.ships;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class Viper extends Ship
{
    public Viper()
    {
        name = "Viper";
        cost = 0;
        set = CardSet.CORE;
        text = "Add 1 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(1);
    }
}
