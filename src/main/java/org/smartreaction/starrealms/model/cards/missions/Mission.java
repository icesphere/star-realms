package org.smartreaction.starrealms.model.cards.missions;

import org.smartreaction.starrealms.model.players.Player;

public abstract class Mission {
    public abstract boolean isMissionCompleted(Player player);

    public abstract void onMissionClaimed(Player player);
}
