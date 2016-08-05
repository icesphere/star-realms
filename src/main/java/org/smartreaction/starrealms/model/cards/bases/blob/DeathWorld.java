package org.smartreaction.starrealms.model.cards.bases.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ScrapCardsForBenefitActionCard;
import org.smartreaction.starrealms.model.cards.actions.ScrapCardsFromHandOrDiscardPileForBenefit;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class DeathWorld extends Base implements ScrapCardsForBenefitActionCard {
    public DeathWorld() {
        name = "Death World";
        addFaction(Faction.BLOB);
        cost = 7;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        shield = 6;
        text = "Add 4 Combat; You may scrap a Trade Federation, Machine Cult or Star Empire card from your hand or discard pile. If you do, draw a card.";
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(4);
        player.addAction(new ScrapCardsFromHandOrDiscardPileForBenefit(this, 1, "You may scrap a Trade Federation, Machine Cult or Star Empire card from your hand or discard pile. If you do, draw a card.", true));
    }

    @Override
    public void cardsScrapped(Player player, List<Card> scrappedCards) {
        player.drawCard();
    }

    @Override
    public boolean isCardApplicable(Card card) {
        return card.isTradeFederation() || card.isMachineCult() || card.isStarEmpire();
    }
}
