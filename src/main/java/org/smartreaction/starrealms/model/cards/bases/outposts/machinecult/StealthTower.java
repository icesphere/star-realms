package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ActionResult;
import org.smartreaction.starrealms.model.cards.actions.CardAction;
import org.smartreaction.starrealms.model.cards.actions.CardActionCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;
import java.util.Set;

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
        if (baseBeingCopied != null) {
            baseBeingCopied.baseUsed(player);
        } else {
            player.addCardAction(this, "Choose a base to copy. Until your turn ends, Stealth Tower becomes a copy of the chosen base. Stealth Tower has that base's faction in addition to Machine Cult.");
        }
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
    public Set<Faction> getFactions() {
        Set<Faction> factions = super.getFactions();
        if (baseBeingCopied != null) {
            factions.addAll(baseBeingCopied.getFactions());
        }
        return factions;
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
        return player.getInPlay().stream().filter(c -> c.isBase() && !(c instanceof StealthTower)).count() > 0;
    }

    @Override
    public void processCardActionResult(CardAction cardAction, Player player, ActionResult result) {
        Card selectedCard = result.getSelectedCard();
        if (selectedCard != null) {
            try {
                baseBeingCopied = (Base) selectedCard.getClass().newInstance();
                baseBeingCopied.setCopied(true);
                player.addGameLog(player.getPlayerName() + " copied " + baseBeingCopied.getName());
                player.playCard(baseBeingCopied);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isUsed() {
        if (baseBeingCopied != null) {
            return baseBeingCopied.isUsed();
        } else {
            return super.isUsed();
        }
    }

    @Override
    public void setUsed(boolean used) {
        if (baseBeingCopied != null) {
            baseBeingCopied.setUsed(used);
        } else {
            super.setUsed(used);
        }
    }

    @Override
    public boolean baseCanBeUsed(Player player) {
        if (baseBeingCopied != null) {
            return super.baseCanBeUsed(player);
        } else {
            return super.baseCanBeUsed(player);
        }
    }

    @Override
    public boolean isAutoUse() {
        if (baseBeingCopied != null) {
            return baseBeingCopied.isAutoUse();
        } else {
            return super.isAutoUse();
        }
    }

    @Override
    public boolean isActionable(Player player, String cardLocation) {
        if (baseBeingCopied != null) {
            return baseBeingCopied.isActionable(player, cardLocation);
        } else {
            return super.isActionable(player, cardLocation);
        }
    }

    @Override
    public void onEndTurn() {
        super.onEndTurn();
        baseBeingCopied = null;
    }

    @Override
    public boolean isShowDoNotUse() {
        return true;
    }

    @Override
    public boolean isAlliableCard() {
        if (baseBeingCopied != null) {
            return baseBeingCopied.isAlliableCard();
        } else {
            return super.isAlliableCard();
        }
    }

    @Override
    public boolean isAlliedAbilityUsed(Faction faction) {
        if (baseBeingCopied != null) {
            return baseBeingCopied.isAlliedAbilityUsed(faction);
        } else {
            return super.isAlliedAbilityUsed(faction);
        }
    }

    @Override
    public void setAlliedAbilityUsed(boolean used, Faction faction) {
        if (baseBeingCopied != null) {
            baseBeingCopied.setAlliedAbilityUsed(used, faction);
        } else {
            super.setAlliedAbilityUsed(used, faction);
        }
    }

    @Override
    public boolean isScrappable() {
        if (baseBeingCopied != null) {
            return baseBeingCopied.isScrappable();
        } else {
            return super.isScrappable();
        }
    }
}
