package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

public class Exterminate extends Mission {
    @Override
    public boolean isMissionCompleted(Player player) {
        return PlayShipWhileBaseInPlay.isMissionCompleted(player, Faction.BLOB);
    }

    @Override
    public void onMissionClaimed(Player player) {

    }
}
