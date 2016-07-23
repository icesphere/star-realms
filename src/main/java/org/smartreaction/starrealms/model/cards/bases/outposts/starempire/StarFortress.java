package org.smartreaction.starrealms.model.cards.bases.outposts.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class StarFortress extends Outpost implements AlliableCard
{
    public StarFortress()
    {
        name = "Star Fortress";
        faction = Faction.STAR_EMPIRE;
        cost = 7;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        shield = 6;
        text = "Add 3 Combat; Draw a card, then discard a card; Ally: Draw a card, then discard a card";
        autoAlly = false;
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(3);
        player.drawCard();
        player.discardCardFromHand();
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
        player.discardCardFromHand();
    }
}
