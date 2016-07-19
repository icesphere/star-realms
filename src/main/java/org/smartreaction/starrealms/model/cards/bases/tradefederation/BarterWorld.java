package org.smartreaction.starrealms.model.cards.bases.tradefederation;


import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class BarterWorld extends Base implements ScrappableCard, ChoiceActionCard
{
    public BarterWorld()
    {
        name = "Barter World";
        faction = Faction.TRADE_FEDERATION;
        cost = 4;
        set = CardSet.CORE;
        shield = 4;
        text = "Add 2 Authority OR Add 2 Trade; Scrap: Add 5 Combat";
    }

    @Override
    public void baseUsed(Player player) {
        Choice choice1 = new Choice(1, "Add 2 Authority");
        Choice choice2 = new Choice(2, "Add 2 Trade");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.addCombat(5);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 5;
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 2 Authority");
            player.addAuthority(2);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 2 Trade");
            player.addTrade(2);
        }
    }
}
