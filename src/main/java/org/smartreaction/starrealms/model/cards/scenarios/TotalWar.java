package org.smartreaction.starrealms.model.cards.scenarios;

import org.smartreaction.starrealms.model.Game;

public class TotalWar extends OncePerTurnScenario {

    public TotalWar() {
        super("Total War", "Once per turn, a player may spend 1 trade to gain 3 combat.", "Spend 1 trade to gain 3 combat");
    }

    @Override
    public boolean isActionAvailable(Game game) {
        return game.getCurrentPlayer().getTrade() > 0;
    }

    @Override
    public void processAction(Game game) {
        game.getCurrentPlayer().addTrade(-1);
        game.getCurrentPlayer().addCombat(3);
    }
}
