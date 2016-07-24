package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.players.Player;

public class FrontierFleet extends Gambit implements EveryTurnGambit {
    public FrontierFleet() {
        name = "Frontier Fleet";
        text = "Each turn gain 1 Combat";
    }

    @Override
    public void everyTurnAbility(Player player) {
        player.addCombat(1);
    }
}
