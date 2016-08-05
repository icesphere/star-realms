package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Battlecruiser extends Ship implements ScrappableCard, AlliableCard
{
    public Battlecruiser()
    {
        name = "Battlecruiser";
        addFaction(Faction.STAR_EMPIRE);
        cost = 6;
        set = CardSet.CORE;
        text = "Add 5 Combat; Draw a card; Ally: Target Opponent discards a card; Scrap: Draw a card. You may destroy target base.";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(5);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.opponentDiscardsCard();
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.drawCard();
        player.destroyTargetBase();
    }

    @Override
    public boolean canDestroyBasedWhenScrapped() {
        return true;
    }
}
