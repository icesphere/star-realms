package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.CardCopier;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ActionResult;
import org.smartreaction.starrealms.model.cards.actions.CardAction;
import org.smartreaction.starrealms.model.cards.actions.CardActionCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

import java.util.*;

public class StealthTower extends Outpost implements CardActionCard, CardCopier
{
    private Base cardBeingCopied;

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
    public void resetCard() {
        super.resetCard();
        cardBeingCopied = null;
    }

    @Override
    public void resetTo(Card card) {
        super.resetTo(card);
        StealthTower stealthTower = (StealthTower) card;
        if (stealthTower.getCardBeingCopied() != null) {
            cardBeingCopied = (Base) stealthTower.getCardBeingCopied().copyCardForSimulation();
        } else {
            cardBeingCopied = null;
        }
    }

    @Override
    public Card copyCardForSimulation() {
        StealthTower card = (StealthTower) super.copyCardForSimulation();
        if (cardBeingCopied != null) {
            card.setCardBeingCopied((Base) cardBeingCopied.copyCardForSimulation());
        }
        return card;
    }

    @Override
    public void baseUsed(Player player) {
        if (cardBeingCopied != null) {
            cardBeingCopied.baseUsed(player);
        } else {
            player.addCardAction(this, "Choose a base to copy. Until your turn ends, Stealth Tower becomes a copy of the chosen base. Stealth Tower has that base's faction in addition to Machine Cult.");
        }
    }

    @Override
    public Base getCardBeingCopied() {
        return cardBeingCopied;
    }

    public void setCardBeingCopied(Base cardBeingCopied) {
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
        return card.isBase()
                && (cardLocation.equals(Card.CARD_LOCATION_PLAYER_BASES) || cardLocation.equals(CARD_LOCATION_OPPONENT_BASES))
                && !(card instanceof StealthTower);
    }

    @Override
    public boolean processCardAction(Player player) {
        List<Card> cards = new ArrayList<>();
        cards.addAll(player.getBases());
        cards.addAll(player.getOpponent().getBases());
        return !cards.isEmpty() && cards.stream().anyMatch(c -> !(c instanceof StealthTower));
    }

    @Override
    public void processCardActionResult(CardAction cardAction, Player player, ActionResult result) {
        Card selectedCard = result.getSelectedCard();
        if (selectedCard != null) {
            try {
                cardBeingCopied = (Base) selectedCard.getClass().newInstance();
                cardBeingCopied.setCopied(true);
                player.addGameLog(player.getPlayerName() + " copied " + cardBeingCopied.getName());
                player.playCard(cardBeingCopied);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isUsed() {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isUsed();
        } else {
            return super.isUsed();
        }
    }

    @Override
    public void setUsed(boolean used) {
        if (cardBeingCopied != null) {
            cardBeingCopied.setUsed(used);
        } else {
            super.setUsed(used);
        }
    }

    @Override
    public boolean baseCanBeUsed(Player player) {
        if (cardBeingCopied != null) {
            return super.baseCanBeUsed(player);
        } else {
            return super.baseCanBeUsed(player);
        }
    }

    @Override
    public boolean isAutoUse() {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isAutoUse();
        } else {
            return super.isAutoUse();
        }
    }

    @Override
    public boolean isActionable(Player player, String cardLocation) {
        if (cardBeingCopied != null) {
            return cardBeingCopied.isActionable(player, cardLocation);
        } else {
            //noinspection SimplifiableIfStatement
            if (Objects.equals(cardLocation, CARD_LOCATION_PLAYER_BASES)
                    && player.getBases().size() == 1
                    && player.getOpponent().getBases().size() == 0) {
                return false;
            }
            return super.isActionable(player, cardLocation);
        }
    }

    @Override
    public void onEndTurn() {
        super.onEndTurn();
        cardBeingCopied = null;
    }

    @Override
    public boolean isShowDoNotUse() {
        return true;
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
