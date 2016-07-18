package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class ScrapCardsFromTradeRow extends Action {
    private int numCardsToScrap;

    private List<Card> selectedCards = new ArrayList<>(3);

    public ScrapCardsFromTradeRow(int numCardsToScrap) {
        this.numCardsToScrap = numCardsToScrap;
        this.text = "Scrap up to " + numCardsToScrap + " from the trade row";
    }

    public int getNumCardsToScrap() {
        return numCardsToScrap;
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return cardLocation.equals(Card.CARD_LOCATION_TRADE_ROW) && !selectedCards.contains(card);
    }

    @Override
    public boolean processAction(Player player) {
        if (player.getGame().getTradeRow().isEmpty()) {
            return false;
        } else {
            if (numCardsToScrap > player.getGame().getTradeRow().size()) {
                numCardsToScrap = player.getGame().getTradeRow().size();
            }
            return true;
        }
    }

    @Override
    public void processActionResult(Player player, ActionResult result) {
        selectedCards.forEach(player.getGame()::scrapCardFromTradeRow);
    }

    @Override
    public boolean showActionDialog() {
        return selectedCards.size() == 0;
    }
}
