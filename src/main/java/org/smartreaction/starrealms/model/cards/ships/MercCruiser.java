package org.smartreaction.starrealms.model.cards.ships;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.players.Player;

import java.util.HashSet;
import java.util.Set;

public class MercCruiser extends Ship implements ChoiceActionCard
{
    Faction factionChoice;

    public MercCruiser()
    {
        name = "Merc Cruiser";
        cost = 3;
        set = CardSet.PROMO_YEAR_1;
        text = "Add 5 Combat; Choose a faction as you play Merc Cruiser. Merc Cruiser has that faction";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);

        Choice choice1 = new Choice(1, "Trade Federation");
        Choice choice2 = new Choice(2, "Blob");
        Choice choice3 = new Choice(3, "Star Empire");
        Choice choice4 = new Choice(4, "Machine Cult");

        player.makeChoice(this, choice1, choice2, choice3, choice4);
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        switch (choice) {
            case 1:
                factionChoice = Faction.TRADE_FEDERATION;
                break;
            case 2:
                factionChoice = Faction.BLOB;
                break;
            case 3:
                factionChoice = Faction.STAR_EMPIRE;
                break;
            case 4:
                factionChoice = Faction.MACHINE_CULT;
                break;
        }

        player.allyInPlayCards();
    }

    @Override
    public Set<Faction> getFactions() {
        Set<Faction> factions = new HashSet<>();
        if (factionChoice != null && !factions.contains(factionChoice)) {
            factions.add(factionChoice);
        }
        return factions;
    }

    @Override
    public void removedFromPlay(Player player) {
        factionChoice = null;
    }

    @Override
    public void resetCard() {
        super.resetCard();
        factionChoice = null;
    }

    @Override
    public Card copyCardForSimulation() {
        MercCruiser card = (MercCruiser) super.copyCardForSimulation();
        card.factionChoice = factionChoice;
        return card;
    }
}
