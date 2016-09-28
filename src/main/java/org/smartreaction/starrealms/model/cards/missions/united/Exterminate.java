package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.cards.missions.PlayShipWhileBaseInPlay;
import org.smartreaction.starrealms.model.players.Player;

public class Exterminate extends Mission {
    public Exterminate() {
        name = "Exterminate";
        objectiveText = "Play a Blob ship while you have a Blob base in play.";
        rewardText = "Add 3 Combat. Scrap any number of cards currently in the trade row.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {
        return PlayShipWhileBaseInPlay.isMissionCompleted(player, Faction.BLOB);
    }

    @Override
    public void onMissionClaimed(Player player) {
        player.addCombat(3);
        player.optionallyScrapCardsInTradeRow(player.getGame().getTradeRow().size());
    }
}
