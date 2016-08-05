package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ActionResult;
import org.smartreaction.starrealms.model.cards.actions.CardAction;
import org.smartreaction.starrealms.model.cards.actions.CardActionCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class StealthNeedle extends Ship implements CardActionCard
{
    private Card cardBeingCopied;

    public StealthNeedle()
    {
        name = "Stealth Needle";
        addFaction(Faction.MACHINE_CULT);
        cost = 4;
        set = CardSet.CORE;
        text = "Copy another ship you've played this turn. Stealth Needle has that ship's faction in addition to Machine Cult.";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addAction(new CardAction(this, "Copy another ship you've played this turn. Stealth Needle has that ship's faction in addition to Machine Cult."));
    }

    public Card getCardBeingCopied() {
        return cardBeingCopied;
    }

    @Override
    public List<Faction> getAlliedFactions(Card card) {
        List<Faction> alliedFactions = super.getAlliedFactions(card);
        if (cardBeingCopied != null) {
            alliedFactions.addAll(cardBeingCopied.getAlliedFactions(card));
        }
        return alliedFactions;
    }

    @Override
    public boolean isBlob() {
        return cardBeingCopied != null && cardBeingCopied.isBlob();
    }

    @Override
    public boolean isTradeFederation() {
        return cardBeingCopied != null && cardBeingCopied.isTradeFederation();
    }

    @Override
    public boolean isStarEmpire() {
        return cardBeingCopied != null && cardBeingCopied.isStarEmpire();
    }

    @Override
    public void removedFromPlay(Player player) {
        cardBeingCopied = null;
    }

    @Override
    public boolean isCardActionable(Card card, CardAction cardAction, String cardLocation, Player player) {
        return card.isShip() && cardLocation.equals(Card.CARD_LOCATION_PLAY_AREA) && !(card instanceof StealthNeedle);
    }

    @Override
    public boolean processCardAction(Player player) {
        return player.getInPlay().stream().filter(c -> c.isShip() && !(c instanceof StealthNeedle)).count() > 0;
    }

    @Override
    public void processCardActionResult(CardAction cardAction, Player player, ActionResult result) {
        Card selectedCard = result.getSelectedCard();
        if (selectedCard != null) {
            try {
                Card shipToCopyCopy = selectedCard.getClass().newInstance();
                cardBeingCopied = shipToCopyCopy;
                player.playCard(shipToCopyCopy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
