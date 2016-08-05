package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class ImperialFighter extends Ship implements AlliableCard
{
    public ImperialFighter()
    {
        name = "Imperial Fighter";
        addFaction(Faction.STAR_EMPIRE);
        cost = 1;
        set = CardSet.CORE;
        text = "Add 2 Combat; Target Opponent discards a card; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(2);
        player.opponentDiscardsCard();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(2);
    }
}
