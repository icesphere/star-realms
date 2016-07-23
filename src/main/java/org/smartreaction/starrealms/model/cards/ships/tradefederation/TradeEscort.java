package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class TradeEscort extends Ship implements AlliableCard
{
    public TradeEscort()
    {
        name = "Trade Escort";
        faction = Faction.TRADE_FEDERATION;
        cost = 5;
        set = CardSet.CORE;
        text = "Add 4 Authority; Add 4 Combat; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addAuthority(4);
        player.addCombat(4);
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
