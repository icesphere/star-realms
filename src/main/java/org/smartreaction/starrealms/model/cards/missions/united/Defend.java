package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

public class Defend extends Mission {
    public Defend() {
        name = "Defend";
        objectiveText = "Have two or more outposts in play.";
        rewardText = "Draw a card. Return target base to its controller's hand.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {
        return player.getOutposts().size() >= 2;
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.drawCard();
        player.returnTargetBaseToHand();
    }
}
