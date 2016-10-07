package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

import java.util.HashSet;
import java.util.Set;

public class Unite extends Mission {
    public Unite() {
        name = "Unite";
        objectiveText = "Play three ships from different factions in the same turn.";
        rewardText = "Add 5 Authority. Draw a card.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {

        Set<Faction> factionsOfShipsPlayedThisTurn = new HashSet<>();

        player.getShipsPlayedThisTurn().forEach(c -> {
            c.getFactions().forEach(factionsOfShipsPlayedThisTurn::add);
        });

        return player.getShipsPlayedThisTurn().size() >= 3 && factionsOfShipsPlayedThisTurn.size() >= 3;
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.addAuthority(5);
        player.drawCard();
    }
}
