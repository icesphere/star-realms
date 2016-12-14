package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class StarBarge extends Ship implements AlliableCard
{
    public StarBarge()
    {
        name = "Star Barge";
        addFaction(Faction.STAR_EMPIRE);
        cost = 1;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Trade; Ally: Add 2 Combat; Target Opponent discards a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(2);
        player.opponentDiscardsCard();
    }
}
