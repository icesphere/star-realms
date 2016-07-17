package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public class FreeCardFromTradeRowToHand extends Action {
    private Integer maxCost;

    public FreeCardFromTradeRowToHand(Integer maxCost, String text) {
        this.maxCost = maxCost;
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_TRADE_ROW) && (maxCost == null || (card.getCost() <= maxCost));
    }

    @Override
    public boolean processAction(Player player) {
        return !player.getGame().getTradeRow().isEmpty();
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        Card card = result.getSelectedCard();
        player.getGame().getTradeRow().remove(card);
        player.getGame().addCardToTradeRow();
        player.addGameLog(player.getPlayerName() + " gained free card from trade row and put it in their hand: " + card.getName());
        player.addCardToHand(card);
    }
}
