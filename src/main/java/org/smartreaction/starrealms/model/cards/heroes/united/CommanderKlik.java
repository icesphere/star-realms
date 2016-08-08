package org.smartreaction.starrealms.model.cards.heroes.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.DiscardCardsForBenefitActionCard;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class CommanderKlik extends Hero implements DiscardCardsForBenefitActionCard {
    public CommanderKlik() {
        name = "Commander Klik";
        set = CardSet.UNITED_HEROES;
        cost = 4;
        text = "Buy: Until end of turn, you may use all of your Star Empire Ally abilities. You may discard a card. If you do, draw a card; Scrap: Until end of turn, you may use all of your Star Empire Ally abilities. You may discard a card. If you do, draw a card.";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.STAR_EMPIRE;
    }

    @Override
    public void heroBought(Player player) {
        player.starEmpireAlliedUntilEndOfTurn();
        player.optionallyDiscardCardsForBenefit(this, 1, "You may discard a card. If you do, draw a card.");
    }

    @Override
    public void cardScrapped(Player player) {
        player.starEmpireAlliedUntilEndOfTurn();
        player.optionallyDiscardCardsForBenefit(this, 1, "You may discard a card. If you do, draw a card.");
    }

    @Override
    public void cardsDiscarded(Player player, List<Card> discardedCards) {
        if (!discardedCards.isEmpty()) {
            player.drawCard();
        }
    }

    @Override
    public void onChoseDoNotUse(Player player) {
        //do nothing
    }
}
