package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

public class Dominate extends Mission {
    public Dominate() {
        name = "Dominate";
        objectiveText = "Play a Star Empire ship while you have a Star Empire base in play.";
        rewardText = "Add 3 Combat. Draw a card.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {
        return PlayShipWhileBaseInPlay.isMissionCompleted(player, Faction.STAR_EMPIRE);
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.addCombat(3);
        player.drawCard();
    }
}
