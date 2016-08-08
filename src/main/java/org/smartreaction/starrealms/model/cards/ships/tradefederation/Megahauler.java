package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Megahauler extends Ship implements AlliableCard
{
    public Megahauler()
    {
        name = "Megahauler";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 7;
        set = CardSet.PROMO_YEAR_1;
        text = "Add 5 Authority; Acquire any ship without paying its cost and put it on top of your deck; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addAuthority(5);
        player.acquireFreeShipToTopOfDeck();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }
}
