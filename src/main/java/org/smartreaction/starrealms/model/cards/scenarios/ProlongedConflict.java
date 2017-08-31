package org.smartreaction.starrealms.model.cards.scenarios;

import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.players.Player;

public class ProlongedConflict extends Scenario implements StartOfGameScenario {

    public ProlongedConflict() {
        super("Prolonged Conflict", "At the start of the game each player gains 30 authority.");
    }

    @Override
    public void applyScenarioToGame(Game game) {
        for (Player player : game.getPlayers()) {
            player.addAuthority(30);
        }
    }
}
