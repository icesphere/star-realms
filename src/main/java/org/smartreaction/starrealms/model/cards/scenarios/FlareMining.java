package org.smartreaction.starrealms.model.cards.scenarios;

import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.players.Player;

public class FlareMining extends OncePerTurnScenario {

    public FlareMining() {
        super("Flare Mining", "Once per turn, a player may spend 1 trade to draw a card, then discard a card.", "Spend 1 trade to draw a card, then discard a card");
    }

    @Override
    public boolean isActionAvailable(Game game) {
        Player player = game.getCurrentPlayer();
        return player.getTrade() > 0 && (player.getDeck().size() > 0 || player.getDiscard().size() > 0);
    }

    @Override
    public void processAction(Game game) {
        game.getCurrentPlayer().drawCard();
        game.getCurrentPlayer().discardCardFromHand();
    }
}
