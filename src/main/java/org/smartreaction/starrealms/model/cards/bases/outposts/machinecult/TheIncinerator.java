package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ScrapCardsForBenefitActionCard;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class TheIncinerator extends Outpost implements AlliableCard, ScrapCardsForBenefitActionCard
{
    private int cardsScrapped;

    public TheIncinerator()
    {
        name = "The Incinerator";
        faction = Faction.MACHINE_CULT;
        cost = 8;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Scrap up to two cards from your hand and/or discard pile. Ally: Gain 2 Combat for each card scrapped from your hand and/or discard pile this turn.";
    }

    @Override
    public void baseUsed(Player player)
    {
        player.optionallyScrapCardsFromHandOrDiscard(2);
    }

    @Override
    public void cardAllied(Player player) {
        //todo track all cards scrapped
        player.addCombat(2 * cardsScrapped);
    }

    @Override
    public void cardsScrapped(Player player, List<Card> scrappedCards) {
        cardsScrapped = scrappedCards.size();
    }
}
