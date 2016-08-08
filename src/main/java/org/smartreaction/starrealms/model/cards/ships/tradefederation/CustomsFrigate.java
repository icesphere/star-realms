package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class CustomsFrigate extends Ship implements AlliableCard, ScrappableCard
{
    public CustomsFrigate()
    {
        name = "Customs Frigate";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 4;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        text = "You may acquire a ship of cost four or less and put it on top of your deck; Ally: Add 4 Combat; Scrap: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.acquireFreeShipToTopOfDeck(4);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(4);
    }

    @Override
    public void cardScrapped(Player player) {
        player.drawCard();
    }
}
