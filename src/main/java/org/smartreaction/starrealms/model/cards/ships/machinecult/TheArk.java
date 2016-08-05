package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ScrapCardsForBenefitActionCard;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class TheArk extends Ship implements ScrappableCard, ScrapCardsForBenefitActionCard
{
    public TheArk()
    {
        name = "The Ark";
        addFaction(Faction.MACHINE_CULT);
        cost = 7;
        set = CardSet.PROMO_YEAR_1;
        text = "Add 5 Combat; Scrap up to two cards in your hand and/or discard pile. Draw a card for each card scrapped this way; Scrap: Destroy target base";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
        player.optionallyScrapCardsFromHandOrDiscardForBenefit(this, 2);
    }

    @Override
    public void cardScrapped(Player player) {
        player.destroyTargetBase();
    }

    @Override
    public boolean canDestroyBasedWhenScrapped() {
        return true;
    }

    @Override
    public void cardsScrapped(Player player, List<Card> scrappedCards) {
        player.drawCards(scrappedCards.size());
    }

    @Override
    public boolean isCardApplicable(Card card) {
        return true;
    }
}
