package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class ScrapCardFromHand extends Action {
    public ScrapCardFromHand(String text) {
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_HAND);
    }

    @Override
    public boolean processAction(Player player) {
        if (player.getHand().isEmpty()) {
            return false;
        } else {
            player.addGameLog(player.getPlayerName() + " is scrapping a card from their hand");
            return true;
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        player.scrapCardFromHand(result.getSelectedCard());
    }
}
