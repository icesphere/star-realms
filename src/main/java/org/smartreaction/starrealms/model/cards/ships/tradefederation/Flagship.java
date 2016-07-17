package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Flagship extends Ship implements AlliableCard
{
    public Flagship()
    {
        name = "Flagship";
        faction = Faction.TRADE_FEDERATION;
        cost = 6;
        set = CardSet.CORE;
        text = "Add 5 Combat; Draw a card; Ally: Add 5 Authority";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.addAuthority(5);
    }
}
