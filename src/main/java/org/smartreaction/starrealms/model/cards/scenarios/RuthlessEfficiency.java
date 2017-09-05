package org.smartreaction.starrealms.model.cards.scenarios;

import org.smartreaction.starrealms.model.Game;

public class RuthlessEfficiency extends OncePerTurnScenario {

    public RuthlessEfficiency() {
        super("Ruthless Efficiency", "Once per turn, a player may spend 1 trade to scrap a card in their hand.", "Spend 1 trade to scrap a card in your hand");
    }

    @Override
    public boolean isActionAvailable(Game game) {
        return game.getCurrentPlayer().getTrade() > 0 && !game.getCurrentPlayer().getHand().isEmpty();
    }

    @Override
    public void processAction(Game game) {
        game.getCurrentPlayer().scrapCardFromHand(false);
    }
}
