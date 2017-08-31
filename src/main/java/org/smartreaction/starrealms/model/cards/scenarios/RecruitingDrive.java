package org.smartreaction.starrealms.model.cards.scenarios;

import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.players.Player;

public class RecruitingDrive extends Scenario implements StartOfGameScenario {

    public RecruitingDrive() {
        super("Recruiting Drive", "Whenever a player acquires a ship, they put it on top of their deck. Bases cost 1 trade less to acquire.");
    }

    @Override
    public void applyScenarioToGame(Game game) {

        for (Player player : game.getPlayers()) {
            player.setCardCostModifier((c, p) -> {
                if (c.isBase()) {
                    return c.getCost() - 1;
                }

                return c.getCost();
            });
        }
    }
}
