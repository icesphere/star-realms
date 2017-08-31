package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.players.Player;

public class FreeCardFromTradeRow extends Action {

    private Integer maxCost;

    private String destination;

    private boolean onlyShips;

    private boolean includeHeroes;

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

    public FreeCardFromTradeRow(Integer maxCost, String text, String destination, boolean onlyShips, boolean includeHeroes) {
        this.maxCost = maxCost;
        this.text = text;
        this.destination = destination;
        this.onlyShips = onlyShips;
        this.includeHeroes = includeHeroes;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return (cardLocation.equals(Card.CARD_LOCATION_EXPLORERS) || cardLocation.equals(Card.CARD_LOCATION_TRADE_ROW))
                && (maxCost == null || (player.getCardCostWithModifiers(card) <= maxCost))
                && (includeHeroes || card.isShip() || card.isBase())
                && (!onlyShips || card.isShip());
    }

    @Override
    public boolean processAction(Player player) {
        if (onlyShips) {
            long numShipsInTradeRow = player.getGame().getTradeRow().stream().filter(Card::isShip).count();
            if (numShipsInTradeRow == 0) {
                return false;
            }
        }

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

        player.addGameLog(player.getPlayerName() + " acquired a free card from the trade row: " + card.getName());

        if (destination.equals(Card.CARD_LOCATION_HAND)) {
            player.acquireCardToHand(card);
        } else if (destination.equals(Card.CARD_LOCATION_DECK)) {
            player.acquireCardToTopOfDeck(card);
        } else {
            player.cardAcquired(card);
        }

        return true;
    }

    @Override
    public boolean isShowDoNotUse() {
        return true;
    }
}
