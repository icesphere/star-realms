package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ScrapCardsForBenefitActionCard;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class BrainWorld extends Outpost implements ScrapCardsForBenefitActionCard
{
    public BrainWorld()
    {
        name = "Brain World";
        faction = Faction.MACHINE_CULT;
        cost = 8;
        set = CardSet.CORE;
        shield = 6;
        text = "Scrap up to two cards from your hand and/or discard pile. Draw a card for each card scrapped this way.";
    }

    @Override
    public void baseUsed(Player player)
    {
        player.optionallyScrapCardsFromHandOrDiscardForBenefit(this, 2);
    }

    @Override
    public void cardsScrapped(Player player, List<Card> scrappedCards) {
        player.drawCards(scrappedCards.size());
    }
}
