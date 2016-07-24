package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class TheIncinerator extends Outpost implements AlliableCard
{
    public TheIncinerator()
    {
        name = "The Incinerator";
        faction = Faction.MACHINE_CULT;
        cost = 8;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Scrap up to two cards from your hand and/or discard pile. Ally: Gain 2 Combat for each card scrapped from your hand and/or discard pile this turn.";
        autoAlly = false;
    }

    @Override
    public void baseUsed(Player player)
    {
        player.optionallyScrapCardsFromHandOrDiscard(2);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2 * player.getNumCardsScrappedThisTurn());
    }
}
