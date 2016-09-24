package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.players.Player;

public class PlayShipWhileBaseInPlay {
    public static boolean isMissionCompleted(Player player, Faction faction) {
        if (player.getInPlay().isEmpty()) {
            return false;
        }

        boolean hasStarEmpireBase = player.getBases().stream().anyMatch(b -> b.hasFaction(faction));

        if (!hasStarEmpireBase) {
            return false;
        }

        Card lastCardPlayed = player.getInPlay().get(player.getInPlay().size() - 1);
        return lastCardPlayed.isShip() && lastCardPlayed.hasFaction(faction);
    }
}
