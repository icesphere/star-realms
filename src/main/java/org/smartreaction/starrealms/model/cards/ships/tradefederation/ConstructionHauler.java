package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class ConstructionHauler extends Ship implements AlliableCard
{
    public ConstructionHauler()
    {
        name = "Construction Hauler";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 6;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 3 Authority; Add 2 Trade; Draw a Card; Ally: You may put the next base you acquire this turn directly into play";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addAuthority(3);
        player.addTrade(2);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.nextBaseToHand();
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }
}
