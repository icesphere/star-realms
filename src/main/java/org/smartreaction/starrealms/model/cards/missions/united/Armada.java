package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.players.Player;

public class Armada extends Mission {
    public Armada() {
        name = "Armada";
        objectiveText = "Play seven or more ships in the same turn.";
        rewardText = "Draw a card. Aquire an Explorer for free and put it into your hand.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {
        return player.getShipsPlayedThisTurn().size() >= 7;
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.drawCard();
        player.setAddShipToHandForMission(true);
        player.cardAcquired(new Explorer());
    }
}
