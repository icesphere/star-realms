package org.smartreaction.starrealms.model.cards.bases.outposts.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class FighterBase extends Outpost implements AlliableCard
{
    public FighterBase()
    {
        name = "Fighter Base";
        addFaction(Faction.STAR_EMPIRE);
        cost = 3;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        shield = 5;
        text = "Ally: Target opponent discards a card";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        //do nothing
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.opponentDiscardsCard();
    }
}
