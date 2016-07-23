package org.smartreaction.starrealms.model.cards.ships;

import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public abstract class Ship extends Card {
    @Override
    public boolean isActionable(Player player, String cardLocation) {
        if (!player.isYourTurn()) {
            return false;
        }

        if (cardLocation.equals(Card.CARD_LOCATION_HAND)) {
            return true;
        }

        if (cardLocation.equals(Card.CARD_LOCATION_PLAY_AREA)) {
            if (this instanceof AlliableCard && !alliedAbilityUsed && player.cardHasAlly(this)) {
                return true;
            }
        }

        return false;
    }
}
