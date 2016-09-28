package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.players.Player;

import java.util.*;

public class Unite extends Mission {
    public Unite() {
        name = "Unite";
        objectiveText = "";
        rewardText = "";
    }

    @Override
    public boolean isMissionCompleted(Player player) {

        Map<Faction, List<Card>> shipsPlayedByFaction = new HashMap<>();

        player.getShipsPlayedThisTurn().forEach(c -> {
            Set<Faction> factions = c.getFactions();
            factions.forEach(f -> {
                List<Card> cards = shipsPlayedByFaction.get(f);
                if (cards == null) {
                    cards = new ArrayList<>();
                }
                cards.add(c);
                shipsPlayedByFaction.put(f, cards);
            });
        });

        return shipsPlayedByFaction.keySet().stream().anyMatch(f -> shipsPlayedByFaction.get(f).size() >= 3);
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.addAuthority(5);
        player.drawCard();
    }
}
