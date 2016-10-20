package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

public abstract class Event extends Card {
    protected Event() {
    }

    @Override
    public void cardPlayed(Player player) {

    }

    public void eventTriggered(Player player) {
        player.showTriggeredEvent(this);
        player.getOpponent().showTriggeredEvent(this);
        player.getGame().gameLog(name + " event triggered");
        handleEvent(player);
        player.getGame().scrapCardFromTradeRow(this);
    }

    public abstract void handleEvent(Player player);

    @Override
    public boolean isActionable(Player player, String cardLocation) {
        return false;
    }
}
