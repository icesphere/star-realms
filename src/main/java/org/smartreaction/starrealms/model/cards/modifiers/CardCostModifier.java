package org.smartreaction.starrealms.model.cards.modifiers;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public interface CardCostModifier {

    int getCardCost(Card card, Player player);

}
