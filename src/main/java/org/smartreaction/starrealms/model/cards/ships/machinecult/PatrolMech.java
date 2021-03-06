package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class PatrolMech extends Ship implements AlliableCard, ChoiceActionCard
{
    public PatrolMech()
    {
        name = "Patrol Mech";
        addFaction(Faction.MACHINE_CULT);
        cost = 4;
        set = CardSet.CORE;
        text = "Add 3 Trade OR Add 5 Combat; Ally: You may scrap a card in your hand or discard pile";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        Choice choice1 = new Choice(1, "Add 3 Trade");
        Choice choice2 = new Choice(2, "Add 5 Combat");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.optionallyScrapCardFromHandOrDiscard();
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 3 Trade");
            player.addTrade(3);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 5 Combat");
            player.addCombat(5);
        }
    }

    @Override
    public int getTradeWhenPlayed() {
        return 3;
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
