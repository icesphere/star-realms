package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

public class Unite extends Mission {
    public Unite() {
        name = "Unite";
        objectiveText = "";
        rewardText = "";
    }

    @Override
    public boolean isMissionCompleted(Player player) {
        return false;
    }

    @Override
    public void onMissionClaimed(Player player) {

    }
}
