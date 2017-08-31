package org.smartreaction.starrealms.model.cards.scenarios;

import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.players.Player;

public class BorderSkirmish extends Scenario implements StartOfGameScenario {

    public BorderSkirmish() {
        super("Border Skirmish", "At the start of the game, each player loses 20 authority.");
    }

    @Override
    public void applyScenarioToGame(Game game) {
        for (Player player : game.getPlayers()) {
            player.addAuthority(-20);
        }
    }
}
