package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class MissileBot extends Ship implements AlliableCard
{
    public MissileBot()
    {
        name = "Missile Bot";
        addFaction(Faction.MACHINE_CULT);
        cost = 2;
        set = CardSet.CORE;
        text = "Add 2 Combat; You may scrap a card in your hand or discard pile; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(2);
        player.optionallyScrapCardFromHandOrDiscard();
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
