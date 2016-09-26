package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

public class Ally extends Mission {
    public Ally() {
        name = "Ally";
        objectiveText = "Use ally abilities from two different factions in the same turn.";
        rewardText = "Acquire a ship or base of cost 4 or less for free and put it on top of your deck.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {
        return player.getFactionsWithAllyAbilitiesUsedThisTurn().size() >= 2;
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.acquireFreeCardToTopOfDeck(4);
    }
}
