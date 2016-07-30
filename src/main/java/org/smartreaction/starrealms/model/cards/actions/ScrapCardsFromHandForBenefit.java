package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.players.Player;

public class ScrapCardsFromHandForBenefit extends ScrapCardsFromHand {
    private ScrapCardsForBenefitActionCard scrapCardsForBenefitActionCard;

    public ScrapCardsFromHandForBenefit(ScrapCardsForBenefitActionCard card, int numCardsToScrap, String text) {
        super(numCardsToScrap, text);
        this.scrapCardsForBenefitActionCard = card;
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        boolean doneWithAction = super.processActionResult(player, result);

        if (doneWithAction) {
            scrapCardsForBenefitActionCard.cardsScrapped(player, selectedCards);
        }

        return doneWithAction;
    }
}
