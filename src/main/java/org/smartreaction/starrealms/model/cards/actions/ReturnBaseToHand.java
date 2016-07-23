package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class ReturnBaseToHand extends Action {
    public ReturnBaseToHand(String text) {
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_PLAYER_BASES) || cardLocation.equals(Card.CARD_LOCATION_OPPONENT_BASES);
    }

    @Override
    public boolean processAction(Player player) {
        if (player.getBases().size() == 0 && player.getOpponent().getBases().size() == 0) {
            return false;
        } else {
            player.addGameLog(player.getPlayerName() + " is returning a base to hand");
            return true;
        }
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        Base base = (Base) result.getSelectedCard();
        if (result.getCardLocation().equals(Card.CARD_LOCATION_OPPONENT_BASES)) {
            player.getOpponent().getBases().remove(base);
            player.getOpponent().addCardToHand(base);
        } else {
            player.getBases().remove(base);
            player.addCardToHand(base);
        }
        return true;
    }

    @Override
    public boolean isShowDoNotUse() {
        return true;
    }
}
