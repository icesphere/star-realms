package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.cards.missions.PlayShipWhileBaseInPlay;
import org.smartreaction.starrealms.model.players.Player;

public class Monopolize extends Mission {
    public Monopolize() {
        name = "Monopolize";
        objectiveText = "Play a Trade Federation ship while you have a Trade Federation base in play.";
        rewardText = "Add 10 Authority.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {
        return PlayShipWhileBaseInPlay.isMissionCompleted(player, Faction.TRADE_FEDERATION);
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.addAuthority(10);
    }
}
