package org.smartreaction.starrealms.model.cards.bases;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class DoNotAttackBase extends Base
{
    public DoNotAttackBase()
    {
        name = "Do Not Attack Base";
        cost = 0;
        set = CardSet.CORE;
        text = "This is a placeholder to show simulation results of not attacking a base";
    }

    @Override
    public void cardPlayed(Player player) {
        //do nothing
    }

    @Override
    public void baseUsed(Player player) {
        //do nothing
    }
}
