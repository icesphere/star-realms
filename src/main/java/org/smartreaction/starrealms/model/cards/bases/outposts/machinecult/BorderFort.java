package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class BorderFort extends Outpost implements AlliableCard, ChoiceActionCard
{
    public BorderFort()
    {
        name = "Border Fort";
        addFaction(Faction.MACHINE_CULT);
        cost = 4;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        shield = 5;
        text = "Add 1 Trade OR Add 2 Combat; Ally: Scrap a card in your hand or discard pile";
        autoAlly = false;
    }

    @Override
    public void baseUsed(Player player) {
        Choice choice1 = new Choice(1, "Add 1 Trade");
        Choice choice2 = new Choice(2, "Add 2 Combat");

        player.makeChoice(this, choice1, choice2);
    }

    public void cardAllied(Player player, Faction faction) {
        player.optionallyScrapCardFromHandOrDiscard();
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 1 Trade");
            player.addTrade(1);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 2 Combat");
            player.addCombat(2);
        }
    }

    @Override
    public int getTradeWhenPlayed() {
        return 1;
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
