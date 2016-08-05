package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class Parasite extends Ship implements AlliableCard, ChoiceActionCard
{
    public Parasite()
    {
        name = "Parasite";
        addFaction(Faction.BLOB);
        cost = 5;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat OR Acquire a card of six or less for free; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player)
    {
        Choice choice1 = new Choice(1, "Add 6 Combat");
        Choice choice2 = new Choice(2, "Acquire card");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.drawCard();
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        player.getGame().gameLog("Chose Add 6 Combat");
        if (choice == 1) {
            player.addCombat(6);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Acquire a card of six or less for free");
            player.acquireFreeCard(6);
        }
    }
}
