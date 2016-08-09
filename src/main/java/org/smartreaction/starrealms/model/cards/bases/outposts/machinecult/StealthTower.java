package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ActionResult;
import org.smartreaction.starrealms.model.cards.actions.CardAction;
import org.smartreaction.starrealms.model.cards.actions.CardActionCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.cards.ships.machinecult.StealthNeedle;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class StealthTower extends Outpost implements CardActionCard
{
    private Base baseBeingCopied;

    public StealthTower()
    {
        name = "Stealth Tower";
        addFaction(Faction.MACHINE_CULT);
        cost = 5;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Until your turn ends, Stealth Tower becomes a copy of any base in play. Stealth Tower has that base's faction in addition to Machine Cult.";
    }

    @Override
    public void baseUsed(Player player) {
        player.addCardAction(this, "Until your turn ends, Stealth Tower becomes a copy of any base in play. Stealth Tower has that base's faction in addition to Machine Cult.");
    }

    public Base getBaseBeingCopied() {
        return baseBeingCopied;
    }

    @Override
    public List<Faction> getAlliedFactions(Card card) {
        List<Faction> alliedFactions = super.getAlliedFactions(card);
        if (baseBeingCopied != null) {
            alliedFactions.addAll(baseBeingCopied.getAlliedFactions(card));
        }
        return alliedFactions;
    }

    @Override
    public boolean isBlob() {
        return baseBeingCopied != null && baseBeingCopied.isBlob();
    }

    @Override
    public boolean isTradeFederation() {
        return baseBeingCopied != null && baseBeingCopied.isTradeFederation();
    }

    @Override
    public boolean isStarEmpire() {
        return baseBeingCopied != null && baseBeingCopied.isStarEmpire();
    }

    @Override
    public void removedFromPlay(Player player) {
        baseBeingCopied = null;
    }

    @Override
    public boolean isCardActionable(Card card, CardAction cardAction, String cardLocation, Player player) {
        return card.isBase()
                && (cardLocation.equals(Card.CARD_LOCATION_PLAYER_BASES) || cardLocation.equals(CARD_LOCATION_OPPONENT_BASES))
                && !(card instanceof StealthTower);
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
                Base baseToCopyCopy = (Base) selectedCard.getClass().newInstance();
                baseBeingCopied = baseToCopyCopy;
                player.playCard(baseToCopyCopy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
