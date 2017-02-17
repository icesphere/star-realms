package org.smartreaction.starrealms.model.cards.bases.outposts.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class ImperialPalace extends Outpost implements AlliableCard
{
    public ImperialPalace()
    {
        name = "Imperial Palace";
        addFaction(Faction.STAR_EMPIRE);
        cost = 7;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Draw a card. Target opponent discards a card. Ally: Add 4 Combat";
    }

    @Override
    public void baseUsed(Player player) {
        player.drawCard();
        player.opponentDiscardsCard();
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(4);
    }
}
