package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

public class Diversify extends Mission implements ChoiceActionCard {
    public Diversify() {
        name = "Diversify";
        objectiveText = "In a single turn, gain: 4 Trade and 5 Combat and 3 Authority";
        rewardText = "4 Trade or 5 Combat or 6 Authority";
    }

    @Override
    public boolean isMissionCompleted(Player player) {
        return player.getTradeGainedThisTurn() >= 4
                && player.getCombatGainedThisTurn() >= 5
                && player.getAuthorityGainedThisTurn() >= 3;
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.makeChoice(this, new Choice(1, "4 Trade"), new Choice(2, "5 Combat"), new Choice(3, "6 Authority"));
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        switch (choice) {
            case 1:
                player.addTrade(4);
                break;
            case 2:
                player.addCombat(5);
                break;
            case 3:
                player.addAuthority(6);
                break;
        }
    }
}
