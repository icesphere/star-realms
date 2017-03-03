package org.smartreaction.starrealms.model.cards.ships;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.players.Player;

public class DoNotScrapCard extends Ship
{
    public DoNotScrapCard()
    {
        name = "Do Not Scrap Card";
        cost = 0;
        set = CardSet.CORE;
        text = "This is a placeholder to show simulation results of not scrapping card";
    }

    @Override
    public void cardPlayed(Player player) {
        //do nothing
    }
}
