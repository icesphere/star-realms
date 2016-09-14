package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class PatrolBot extends Ship implements AlliableCard, ChoiceActionCard
{
    public PatrolBot()
    {
        name = "Patrol Bot";
        addFaction(Faction.MACHINE_CULT);
        cost = 2;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        text = "Add 2 Trade OR Add 4 Combat; Ally: You may scrap a card in your hand or discard pile";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        Choice choice1 = new Choice(1, "Add 2 Trade");
        Choice choice2 = new Choice(2, "Add 4 Combat");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.optionallyScrapCardFromHandOrDiscard();
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 2 Trade");
            player.addTrade(2);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 4 Combat");
            player.addCombat(4);
        }
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
