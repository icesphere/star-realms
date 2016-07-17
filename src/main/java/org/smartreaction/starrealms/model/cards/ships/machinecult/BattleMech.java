package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class BattleMech extends Ship implements AlliableCard
{
    public BattleMech()
    {
        name = "Battle Mech";
        faction = Faction.MACHINE_CULT;
        cost = 5;
        set = CardSet.CORE;
        text = "Add 4 Combat; You may scrap a card in your hand or discard pile; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(4);
        player.scrapCardFromHandOrDiscard();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.drawCard();
    }
}