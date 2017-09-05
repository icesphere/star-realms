package org.smartreaction.starrealms.model.cards.scenarios;

import org.smartreaction.starrealms.model.Game;

public class EmergencyRepairs extends OncePerTurnScenario {

    public EmergencyRepairs() {
        super("Emergency Repairs", "Once per turn, a player may spend 1 trade to shuffle their discard pile into their personal deck.", "Spend 1 trade to shuffle your discard pile into your personal deck");
    }

    @Override
    public boolean isActionAvailable(Game game) {
        return game.getCurrentPlayer().getTrade() > 0;
    }

    @Override
    public void processAction(Game game) {
        game.getCurrentPlayer().addTrade(-1);
        game.getCurrentPlayer().shuffleDiscardIntoDeck();
    }
}
