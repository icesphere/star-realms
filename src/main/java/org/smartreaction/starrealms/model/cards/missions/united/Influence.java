package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.players.Player;

import java.util.*;

public class Influence extends Mission {
    public Influence() {
        name = "Influence";
        objectiveText = "Have at least three ships and/or bases of the same faction in play.";
        rewardText = "Acquire two Explorers for free and put them into your hand.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {

        Map<Faction, List<Card>> inPlayByFaction = new HashMap<>();

        player.getInPlay().forEach(c -> {
            Set<Faction> factions = c.getFactions();
            factions.forEach(f -> {
                List<Card> cards = inPlayByFaction.get(f);
                if (cards == null) {
                    cards = new ArrayList<>();
                }
                cards.add(c);
                inPlayByFaction.put(f, cards);
            });
        });

        return inPlayByFaction.keySet().stream().anyMatch(f -> inPlayByFaction.get(f).size() >= 3);
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.acquireCardToHand(new Explorer());
        player.acquireCardToHand(new Explorer());
    }
}
