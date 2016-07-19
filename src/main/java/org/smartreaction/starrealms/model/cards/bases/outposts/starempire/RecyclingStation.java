package org.smartreaction.starrealms.model.cards.bases.outposts.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.actions.DiscardCardsForBenefitActionCard;
import org.smartreaction.starrealms.model.cards.actions.DiscardCardsFromHandForBenefit;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class RecyclingStation extends Outpost implements DiscardCardsForBenefitActionCard, ChoiceActionCard
{
    public RecyclingStation()
    {
        name = "Recycling Station";
        faction = Faction.STAR_EMPIRE;
        cost = 4;
        set = CardSet.CORE;
        shield = 4;
        text = "Add 1 Trade OR Discard up to two cards, then draw that many cards";
    }

    @Override
    public void baseUsed(Player player) {
        Choice choice1 = new Choice(1, "Add 1 Trade");
        Choice choice2 = new Choice(2, "Discard up to two cards, then draw that many cards");
        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void cardsDiscarded(Player player, List<Card> discardedCards) {
        player.drawCards(discardedCards.size());
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 1 Trade");
            player.addTrade(1);
        } else {
            player.getGame().gameLog("Chose Discard up to two cards, then draw that many cards");
            player.addAction(new DiscardCardsFromHandForBenefit(this, 2, "Discard up to two cards to then draw that many cards"));
        }
    }
}
