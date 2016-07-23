package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.Objects;

public abstract class Gambit extends Card {
    @Override
    public void cardPlayed(Player player) {
    }

    @Override
    public boolean isActionable(Player player, String cardLocation) {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
