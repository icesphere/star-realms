package org.smartreaction.starrealms.model.cards.ships;

import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public abstract class Ship extends Card {
    @Override
    public boolean isActionable(Player player, String cardLocation) {
        return player.isYourTurn() && (cardLocation.equals(Card.CARD_LOCATION_HAND)
                || (cardLocation.equals(Card.CARD_LOCATION_PLAY_AREA)
                && ((this instanceof AlliableCard && !alliedAbilityUsed && player.cardHasAlly(this))
                                || this instanceof ScrappableCard)));
    }
}
