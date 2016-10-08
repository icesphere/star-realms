package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class FrontierStation extends Outpost implements ChoiceActionCard
{
    public FrontierStation()
    {
        name = "Frontier Station";
        addFaction(Faction.MACHINE_CULT);
        cost = 6;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Add 2 Trade OR Add 3 Combat";
    }

    @Override
    public void baseUsed(Player player)
    {
        Choice choice1 = new Choice(1, "Add 2 Trade");
        Choice choice2 = new Choice(2, "Add 3 Combat");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 2 Trade");
            player.addTrade(2);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 3 Combat");
            player.addCombat(3);
        }
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }
}
