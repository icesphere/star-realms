package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class BattleBot extends Ship implements AlliableCard
{
    public BattleBot()
    {
        name = "Battle Bot";
        addFaction(Faction.MACHINE_CULT);
        cost = 1;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Combat; You may scrap a card in your hand; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(2);
        player.scrapCardFromHand(true);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(2);
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
