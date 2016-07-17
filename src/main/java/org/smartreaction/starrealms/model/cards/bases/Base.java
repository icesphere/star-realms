package org.smartreaction.starrealms.model.cards.bases;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public abstract class Base extends Card {
    protected boolean used;

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public void cardPlayed(Player player) {

    }

    public boolean useBase(Player player) {
        if (baseCanBeUsed(player)) {
            this.used = true;
            this.baseUsed(player);
            return true;
        }
        return false;
    }

    public abstract void baseUsed(Player player);

    public boolean baseCanBeUsed(Player player) {
        return true;
    }

    public boolean isActionable(Player player, String cardLocation) {
        return player.isYourTurn() && !used;
    }
}
