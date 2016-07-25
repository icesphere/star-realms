package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.players.Player;

public class FreeCardFromTradeRow extends Action {

    private Integer maxCost;

    private String destination;

    public FreeCardFromTradeRow(Integer maxCost, String text) {
        this.maxCost = maxCost;
        this.text = text;
        this.destination = Card.CARD_LOCATION_DISCARD;
    }

    public FreeCardFromTradeRow(Integer maxCost, String text, String destination) {
        this.maxCost = maxCost;
        this.text = text;
        this.destination = destination;
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
    public boolean processActionResult(Player player, ActionResult result) {
        Card card = result.getSelectedCard();
        if (!(card instanceof Explorer)) {
            player.getGame().getTradeRow().remove(card);
            player.getGame().addCardToTradeRow();
        }
        if (destination.equals(Card.CARD_LOCATION_HAND)) {
            player.addGameLog(player.getPlayerName() + " gained free card from trade row and put it in their hand: " + card.getName());
            player.addCardToHand(card);
        } else if (destination.equals(Card.CARD_LOCATION_DECK)) {
            player.addGameLog(player.getPlayerName() + " gained free card from trade row and put it on top of deck: " + card.getName());
            player.addCardToTopOfDeck(card);
        } else {
            player.addGameLog(player.getPlayerName() + " acquired a free card from the trade row: " + card.getName());
            player.cardAcquired(card);
        }
        return true;
    }

    @Override
    public boolean isShowDoNotUse() {
        return true;
    }
}
