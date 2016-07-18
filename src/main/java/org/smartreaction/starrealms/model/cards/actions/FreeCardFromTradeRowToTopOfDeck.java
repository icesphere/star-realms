package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.players.Player;

public class FreeCardFromTradeRowToTopOfDeck extends Action {
    private Integer maxCost;

    public FreeCardFromTradeRowToTopOfDeck(Integer maxCost, String text) {
        this.maxCost = maxCost;
        this.text = text;
    }

    public Integer getMaxCost() {
        return maxCost;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return (cardLocation.equals(Card.CARD_LOCATION_EXPLORERS) || cardLocation.equals(Card.CARD_LOCATION_TRADE_ROW))
                && (maxCost == null || (card.getCost() <= maxCost));
    }

    @Override
    public boolean processAction(Player player) {
        return !player.getGame().getTradeRow().isEmpty();
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        Card card = result.getSelectedCard();
        if (!(card instanceof Explorer)) {
            player.getGame().getTradeRow().remove(card);
            player.getGame().addCardToTradeRow();
        }
        player.addGameLog(player.getPlayerName() + " gained free card from trade row and put it on top of deck: " + card.getName());
        player.addCardToTopOfDeck(card);
    }
}
