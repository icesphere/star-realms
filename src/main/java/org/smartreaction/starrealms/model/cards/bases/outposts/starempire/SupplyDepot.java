package org.smartreaction.starrealms.model.cards.bases.outposts.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.actions.DiscardCardsForBenefitActionCard;
import org.smartreaction.starrealms.model.cards.actions.DiscardCardsFromHandForBenefit;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class SupplyDepot extends Outpost implements AlliableCard, ChoiceActionCard, DiscardCardsForBenefitActionCard
{
    public SupplyDepot()
    {
        name = "Supply Depot";
        faction = Faction.STAR_EMPIRE;
        cost = 6;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Discard up to two cards. Gain 2 Trade or 2 Combat for each card discarded this way. Ally: Draw a card.";
        autoAlly = false;
    }

    @Override
    public void baseUsed(Player player) {
        player.addAction(new DiscardCardsFromHandForBenefit(this, 2, "", true));
    }

    @Override
    public void cardsDiscarded(Player player, List<Card> discardedCards) {
        for (Card ignored : discardedCards) {
            Choice choice1 = new Choice(1, "Add 2 Trade");
            Choice choice2 = new Choice(2, "Add 2 Combat");

            player.makeChoice(this, choice1, choice2);
        }
    }

    @Override
    public void onChoseDoNotUse(Player player) {

    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Gain 2 Trade");
            player.addTrade(2);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Gain 2 Combat");
            player.addCombat(2);
        }
    }
}
