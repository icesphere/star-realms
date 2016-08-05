package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class CommandShip extends Ship implements AlliableCard
{
    public CommandShip()
    {
        name = "Command Ship";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 8;
        set = CardSet.CORE;
        text = "Add 4 Authority; Add 5 Combat; Draw 2 Cards; Ally: You may destroy target base";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addAuthority(4);
        player.addCombat(5);
        player.drawCards(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.destroyTargetBase();
    }
}
