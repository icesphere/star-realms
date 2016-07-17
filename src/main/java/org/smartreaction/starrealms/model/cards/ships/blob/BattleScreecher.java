package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class BattleScreecher extends Ship implements AlliableCard
{
    public BattleScreecher()
    {
        name = "Battle Screecher";
        faction = Faction.BLOB;
        cost = 4;
        set = CardSet.PROMO_YEAR_1;
        text = "Add 5 Combat; You may scrap up to five cards currently in the trade row; Ally: Add 2 Trade";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(5);
        player.scrapCardsInTradeRow(5);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addTrade(2);
    }
}
