package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Gunship extends Ship implements ScrappableCard
{
    public Gunship()
    {
        name = "Gunship";
        addFaction(Faction.STAR_EMPIRE);
        cost = 4;
        set = CardSet.COLONY_WARS;
        text = "Add 5 Combat; Target Opponent discards a card; Scrap: Add 4 Trade";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
        player.opponentDiscardsCard();
    }

    @Override
    public void cardScrapped(Player player) {
        player.addTrade(4);
    }

    @Override
    public int getTradeWhenScrapped() {
        return 4;
    }
}
