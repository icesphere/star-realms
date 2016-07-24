package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class DestroyOwnBase extends Action {
    private DestroyOwnBaseActionCard destroyOwnBaseActionCard;

    public DestroyOwnBase(DestroyOwnBaseActionCard destroyOwnBaseActionCard, String text) {
        this.destroyOwnBaseActionCard = destroyOwnBaseActionCard;
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_PLAYER_BASES);
    }

    @Override
    public boolean processAction(Player player) {
        return player.getBases().size() != 0;
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        player.getOpponent().baseDestroyed((Base) result.getSelectedCard());
        return true;
    }

    @Override
    public boolean isShowDoNotUse() {
        return true;
    }

    @Override
    public void onNotUsed(Player player) {
        destroyOwnBaseActionCard.onNotUsed(player);
    }
}
