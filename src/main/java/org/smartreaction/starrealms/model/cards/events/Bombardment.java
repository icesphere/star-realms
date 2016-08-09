package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.actions.DestroyOwnBaseActionCard;
import org.smartreaction.starrealms.model.players.Player;

public class Bombardment extends Event implements DestroyOwnBaseActionCard {
    public Bombardment() {
        name = "Bombardment";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player either destroys a base they control or loses 6 Authority";
    }

    @Override
    public void handleEvent(Player player) {
        player.destroyOwnBase(this, "Destroy a base you control or lose 6 Authority");
        player.getOpponent().destroyOwnBase(this, "Destroy a base you control or lose 6 Authority");
    }

    @Override
    public void onNotUsed(Player player) {
        player.loseAuthorityFromEvent(6);
    }
}
