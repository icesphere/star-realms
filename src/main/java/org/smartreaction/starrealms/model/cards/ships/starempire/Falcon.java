package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Falcon extends Ship implements ScrappableCard
{
    public Falcon()
    {
        name = "Falcon";
        faction = Faction.STAR_EMPIRE;
        cost = 3;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Combat; Draw a card; Scrap: Target Opponent discards a card";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(2);
        player.drawCard();
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.opponentDiscardsCard();
    }
}
