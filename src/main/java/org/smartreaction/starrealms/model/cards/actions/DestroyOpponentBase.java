package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class DestroyOpponentBase extends Action {
    public DestroyOpponentBase(String text) {
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_OPPONENT_BASES) && card.isBase();
    }

    @Override
    public boolean processAction(Player player) {
        if (player.getOpponent().getNumBases() == 0) {
            return false;
        } else {
            player.addGameLog(player.getPlayerName() + " is choosing an opponent's base to destroy");
            return true;
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        player.getOpponent().baseDestroyed((Base) result.getSelectedCard());
    }
}
