package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class AgingBattleship extends Ship implements ScrappableCard, AlliableCard
{
    public AgingBattleship()
    {
        name = "Aging Battleship";
        addFaction(Faction.STAR_EMPIRE);
        cost = 5;
        set = CardSet.COLONY_WARS;
        text = "Add 5 Combat; Ally: Draw a card; Scrap: Add 2 Combat; Draw two cards";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(2);
        player.drawCards(2);
    }
}
