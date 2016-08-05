package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class ImperialTrader extends Ship implements AlliableCard
{
    public ImperialTrader()
    {
        name = "Imperial Trader";
        addFaction(Faction.STAR_EMPIRE);
        cost = 5;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 3 Trade; Draw a card; Ally: Add 4 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(4);
    }
}
