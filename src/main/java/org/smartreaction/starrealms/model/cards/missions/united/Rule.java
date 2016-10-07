package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

import java.util.HashSet;
import java.util.Set;

public class Rule extends Mission {
    public Rule() {
        name = "Rule";
        objectiveText = "Have bases from two or more factions in play";
        rewardText = "Acquire a card of cost 3 or less for free and put it into your hand";
    }

    @Override
    public boolean isMissionCompleted(Player player) {

        Set<Faction> factionsWithBaseInPlay = new HashSet<>();

        player.getBases().forEach(c -> {
            Set<Faction> factions = c.getFactions();
            factions.forEach(factionsWithBaseInPlay::add);
        });

        return player.getBases().size() >=2 && factionsWithBaseInPlay.size() >= 2;
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.acquireFreeCardToHand(3);
    }
}
