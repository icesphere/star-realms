package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

import java.util.*;

public class Colonize extends Mission {
    public Colonize() {
        name = "Colonize";
        objectiveText = "Have two or more bases of the same faction in play.";
        rewardText = "Draw two cards.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {

        Map<Faction, List<Base>> basesByFaction = new HashMap<>();

        player.getBases().forEach(b -> {
            Set<Faction> factions = b.getFactions();
            factions.forEach(f -> {
                List<Base> bases = basesByFaction.get(f);
                if (bases == null) {
                    bases = new ArrayList<>();
                }
                bases.add(b);
                basesByFaction.put(f, bases);
            });
        });

        return basesByFaction.keySet().stream().anyMatch(f -> basesByFaction.get(f).size() >= 2);
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.drawCards(2);
    }
}
