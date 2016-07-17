package org.smartreaction.starrealms.model.cards.ships;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.players.Player;

public class MercCruiser extends Ship
{
    public MercCruiser()
    {
        name = "Merc Cruiser";
        faction = Faction.UNALIGNED;
        cost = 3;
        set = CardSet.PROMO_YEAR_1;
        text = "Add 5 Combat; Choose a faction as you play Merc Cruiser. Merc Cruiser has that faction";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
        player.chooseFactionForCard(this);
    }
}
