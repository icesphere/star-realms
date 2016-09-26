package org.smartreaction.starrealms.model.cards.missions;

import org.smartreaction.starrealms.model.players.Player;

public abstract class Mission {
    protected String name;

    protected String objectiveText;

    protected String rewardText;

    public abstract boolean isMissionCompleted(Player player);

    public abstract void onMissionClaimed(Player player);
}
