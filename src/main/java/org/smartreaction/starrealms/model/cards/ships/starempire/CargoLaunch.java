package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class CargoLaunch extends Ship implements ScrappableCard
{
    public CargoLaunch()
    {
        name = "Cargo Launch";
        addFaction(Faction.STAR_EMPIRE);
        cost = 1;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        text = "Draw a card; Scrap: Add 1 Trade";
    }

    @Override
    public void cardPlayed(Player player) {
        player.drawCard();
    }

    @Override
    public void cardScrapped(Player player) {
        player.addTrade(1);
    }

    @Override
    public int getTradeWhenScrapped() {
        return 1;
    }
}
