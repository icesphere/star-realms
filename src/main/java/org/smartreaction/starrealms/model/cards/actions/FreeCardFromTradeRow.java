package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.players.Player;

public class FreeCardFromTradeRow extends Action {

    private Integer maxCost;

    public FreeCardFromTradeRow(String text) {
        this.text = text;
    }

    public FreeCardFromTradeRow(Integer maxCost, String text) {
        this.maxCost = maxCost;
        this.text = text;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return (cardLocation.equals(Card.CARD_LOCATION_EXPLORERS) || cardLocation.equals(Card.CARD_LOCATION_TRADE_ROW))
                && (maxCost == null || (card.getCost() <= maxCost));
    }

    @Override
    public boolean processAction(Player player) {
        if (player.getGame().getTradeRow().isEmpty()) {
            return false;
        } else {
            player.addGameLog(player.getPlayerName() + " is choosing a free card from the trade row");
            return true;
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        Card card = result.getSelectedCard();
        if (!(card instanceof Explorer)) {
            player.getGame().getTradeRow().remove(card);
            player.getGame().addCardToTradeRow();
        }
        player.addGameLog(player.getPlayerName() + " acquired a free card from the trade row: " + card.getName());
        player.cardAcquired(card);
    }
}
