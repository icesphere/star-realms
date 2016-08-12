package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public interface CardActionCard {
    boolean isCardActionable(Card card, CardAction cardAction, String cardLocation, Player player);

    boolean processCardAction(Player player);

    void processCardActionResult(CardAction cardAction, Player player, ActionResult result);

    boolean isShowDoNotUse();
}
