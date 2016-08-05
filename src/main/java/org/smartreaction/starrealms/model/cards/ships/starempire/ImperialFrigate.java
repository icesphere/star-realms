package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class ImperialFrigate extends Ship implements ScrappableCard, AlliableCard
{
    public ImperialFrigate()
    {
        name = "Imperial Frigate";
        addFaction(Faction.STAR_EMPIRE);
        cost = 3;
        set = CardSet.CORE;
        text = "Add 4 Combat; Target Opponent discards a card; Ally: Add 2 Combat; Scrap: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(4);
        player.opponentDiscardsCard();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(2);
    }

    @Override
    public void cardScrapped(Player player) {
        player.drawCard();
    }
}
