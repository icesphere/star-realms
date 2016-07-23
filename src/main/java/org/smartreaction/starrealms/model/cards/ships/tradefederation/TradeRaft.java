package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class TradeRaft extends Ship implements AlliableCard, ScrappableCard
{
    public TradeRaft()
    {
        name = "Trade Raft";
        faction = Faction.TRADE_FEDERATION;
        cost = 1;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 1 Trade; Ally: Draw a card; Scrap: Add 1 Trade";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(1);
    }

    @Override
    public void cardAllied(Player player) {
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
