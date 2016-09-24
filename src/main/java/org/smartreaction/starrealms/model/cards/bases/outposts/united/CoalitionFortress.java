package org.smartreaction.starrealms.model.cards.bases.outposts.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class CoalitionFortress extends Outpost implements AlliableCard, ChoiceActionCard {
    public CoalitionFortress() {
        name = "Coalition Fortress";
        addFaction(Faction.TRADE_FEDERATION);
        addFaction(Faction.MACHINE_CULT);
        cost = 6;
        set = CardSet.UNITED_COMMAND;
        shield = 6;
        text = "Add 2 Trade; Ally: Add 2 Combat or Add 3 Authority";
        allFactionsAlliedTogether = true;
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.makeChoice(this, new Choice(1, "2 Combat"), new Choice(2, "3 Authority"));
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.addCombat(2);
        } else if (choice == 2) {
            player.addAuthority(3);
        }
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }
}
