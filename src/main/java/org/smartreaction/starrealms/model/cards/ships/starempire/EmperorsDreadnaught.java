package org.smartreaction.starrealms.model.cards.ships.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class EmperorsDreadnaught extends Ship
{
    public EmperorsDreadnaught()
    {
        name = "Emperors Dreadnaught";
        faction = Faction.STAR_EMPIRE;
        cost = 8;
        set = CardSet.COLONY_WARS;
        text = "Add 8 Combat; Draw a card; Target opponent discards a card; When you acquire this card, if you've played a Star Empire card this turn, you may put this card directly into your hand";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(8);
        player.drawCard();
        player.opponentDiscardsCard();
    }
}
