package org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation;


import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class TradingPost extends Outpost implements ScrappableCard, ChoiceActionCard
{
    public TradingPost()
    {
        name = "Trading Post";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 3;
        set = CardSet.CORE;
        shield = 4;
        text = "Add 1 Authority OR Add 1 Trade; Scrap: Add 3 Combat";
    }

    @Override
    public void baseUsed(Player player) {
        Choice choice1 = new Choice(1, "Add 1 Authority");
        Choice choice2 = new Choice(2, "Add 1 Trade");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.addCombat(3);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 3;
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 1 Authority");
            player.addAuthority(1);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 1 Trade");
            player.addTrade(1);
        }
    }
}
