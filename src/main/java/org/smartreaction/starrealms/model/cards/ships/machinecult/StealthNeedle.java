package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.CardCopier;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ActionResult;
import org.smartreaction.starrealms.model.cards.actions.CardAction;
import org.smartreaction.starrealms.model.cards.actions.CardActionCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StealthNeedle extends Ship implements CardActionCard, CardCopier
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
    public Card copyCardForSimulation() {
        StealthNeedle card = (StealthNeedle) super.copyCardForSimulation();
        if (cardBeingCopied != null) {
            card.setCardBeingCopied(cardBeingCopied.copyCardForSimulation());
        }
        return card;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCardAction(this, "Copy another ship you've played this turn. Stealth Needle has that ship's faction in addition to Machine Cult.");
    }

    @Override
    public Card getCardBeingCopied() {
        return cardBeingCopied;
    }

    public void setCardBeingCopied(Card cardBeingCopied) {
        this.cardBeingCopied = cardBeingCopied;
    }

    @Override
    public List<Faction> getAlliedFactions(Card card) {
        List<Faction> alliedFactions = new ArrayList<>(super.getAlliedFactions(card));
        if (cardBeingCopied != null) {
            alliedFactions.addAll(cardBeingCopied.getAlliedFactions(card));
        }
        return alliedFactions;
    }

    @Override
    public Set<Faction> getFactions() {
        Set<Faction> factions = new HashSet<>();
        factions.add(Faction.MACHINE_CULT);
        if (cardBeingCopied != null) {
            factions.addAll(cardBeingCopied.getFactions());
        }
        return factions;
    }

    @Override
    public boolean hasFaction(Faction faction) {
        return (cardBeingCopied != null && cardBeingCopied.hasFaction(faction)) || super.hasFaction(faction);
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
                cardBeingCopied = selectedCard.getClass().newInstance();
                cardBeingCopied.setCopied(true);
                player.addGameLog(player.getPlayerName() + " copied " + cardBeingCopied.getName());
                player.playCard(cardBeingCopied);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isShowDoNotUse() {
        return true;
    }

    @Override
    public boolean isActionable(Player player, String cardLocation) {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isActionable(player, cardLocation);
        } else {
            return super.isActionable(player, cardLocation);
        }
    }

    @Override
    public boolean isAlliableCard() {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isAlliableCard();
        } else {
            return super.isAlliableCard();
        }
    }

    @Override
    public boolean isAlliedAbilityUsed(Faction faction) {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isAlliedAbilityUsed(faction);
        } else {
            return super.isAlliedAbilityUsed(faction);
        }
    }

    @Override
    public void setAlliedAbilityUsed(boolean used, Faction faction) {
        if (cardBeingCopied != null) {
            cardBeingCopied.setAlliedAbilityUsed(used, faction);
        } else {
            super.setAlliedAbilityUsed(used, faction);
        }
    }

    @Override
    public boolean isScrappable() {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isScrappable();
        } else {
            return super.isScrappable();
        }
    }

    @Override
    public AlliableCard getAlliableCard() {
        if (cardBeingCopied != null && cardBeingCopied instanceof AlliableCard) {
            return (AlliableCard) cardBeingCopied;
        }
        return null;
    }

    @Override
    public boolean isAllFactionsAlliedTogether() {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isAllFactionsAlliedTogether();
        } else {
            return super.isAllFactionsAlliedTogether();
        }
    }

    @Override
    public boolean isAutoAlly() {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isAutoAlly();
        } else {
            return super.isAutoAlly();
        }
    }

    @Override
    public boolean hasUnusedAllyAbility() {
        if (cardBeingCopied != null) {
            return cardBeingCopied.hasUnusedAllyAbility();
        } else {
            return super.hasUnusedAllyAbility();
        }
    }

    @Override
    public boolean isScrapper() {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isScrapper();
        } else {
            return super.isScrapper();
        }
    }

    @Override
    public String getText() {
        if (cardBeingCopied != null) {
            return "Copying " + cardBeingCopied.getName();
        } else {
            return text;
        }
    }
}
