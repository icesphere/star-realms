package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class ScrapCardsFromHandOrDiscardPileForBenefit extends ScrapCardsFromHandOrDiscardPile {
    private ScrapCardsForBenefitActionCard scrapCardsForBenefitActionCard;

    public ScrapCardsFromHandOrDiscardPileForBenefit(ScrapCardsForBenefitActionCard card, int numCardsToScrap, String text) {
        super(numCardsToScrap, text);
        this.scrapCardsForBenefitActionCard = card;
    }

    public ScrapCardsFromHandOrDiscardPileForBenefit(ScrapCardsForBenefitActionCard card, int numCardsToScrap, String text, boolean optional) {
        super(numCardsToScrap, text);
        this.scrapCardsForBenefitActionCard = card;
        this.optional = optional;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return super.isCardActionable(card, cardLocation, player) && scrapCardsForBenefitActionCard.isCardApplicable(card);
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        boolean doneWithAction = super.processActionResult(player, result);

        if (doneWithAction) {
            scrapCardsForBenefitActionCard.cardsScrapped(player, getAllSelectedCards());
        }

        return doneWithAction;
    }
}
