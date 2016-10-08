package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Leviathan extends Ship implements AlliableCard
{
    public Leviathan()
    {
        name = "Leviathan";
        addFaction(Faction.BLOB);
        cost = 8;
        set = CardSet.COLONY_WARS;
        text = "Add 9 Combat; Draw a card; You may destroy target base; Ally: Acquire a card of three or less for free and put it into your hand";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(9);
        player.drawCard();
        player.destroyTargetBase();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.acquireFreeCardToHand(3, false);
    }
}
