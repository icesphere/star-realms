package org.smartreaction.starrealms.model.cards.bases.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.DiscardCardsForBenefitActionCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class OrbitalPlatform extends Base implements DiscardCardsForBenefitActionCard, AlliableCard
{
    public OrbitalPlatform()
    {
        name = "Orbital Platform";
        addFaction(Faction.STAR_EMPIRE);
        cost = 3;
        set = CardSet.COLONY_WARS;
        shield = 4;
        text = "Discard a card. If you do, draw a card. Ally: Add 3 Combat.";
    }

    @Override
    public void baseUsed(Player player) {
        player.discardCardsForBenefit(this, 1, "Discard a card. If you do, draw a card.");
    }

    @Override
    public void cardsDiscarded(Player player, List<Card> discardedCards) {
        if (!discardedCards.isEmpty()) {
            player.drawCard();
        }
    }

    @Override
    public void onChoseDoNotUse(Player player) {

    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.addCombat(3);
    }
}
