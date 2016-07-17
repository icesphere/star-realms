package org.smartreaction.starrealms.model.cards.bases.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class DeathWorld extends Base
{
    public DeathWorld()
    {
        name = "Death World";
        faction = Faction.BLOB;
        cost = 7;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        shield = 6;
        text = "Add 4 Combat; You may scrap a Trade Federation, Machine Cult or Star Empire card from your hand or discard pile. If you do, draw a card";
    }

    @Override
    public void baseUsed(Player player)
    {
        player.addCombat(4);
        player.handleDeathWorld();
    }
}
