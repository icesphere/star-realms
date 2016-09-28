package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

import java.util.*;

public class Rule extends Mission {
    public Rule() {
        name = "Rule";
        objectiveText = "Have bases from two or more factions in play";
        rewardText = "";
    }

    @Override
    public boolean isMissionCompleted(Player player) {

        Map<Faction, List<Base>> basesInPlayByFaction = new HashMap<>();

        player.getBases().forEach(c -> {
            Set<Faction> factions = c.getFactions();
            factions.forEach(f -> {
                List<Base> cards = basesInPlayByFaction.get(f);
                if (cards == null) {
                    cards = new ArrayList<>();
                }
                cards.add(c);
                basesInPlayByFaction.put(f, cards);
            });
        });

        return basesInPlayByFaction.keySet().stream().anyMatch(f -> basesInPlayByFaction.get(f).size() >= 2);
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.acquireFreeCardToHand(3);
    }
}
