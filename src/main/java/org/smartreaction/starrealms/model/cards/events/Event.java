package org.smartreaction.starrealms.model.cards.events;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ShowTriggeredEvent;
import org.smartreaction.starrealms.model.players.Player;

public abstract class Event extends Card {
    protected Event() {
        faction = Faction.UNALIGNED;
    }

    @Override
    public void cardPlayed(Player player) {

    }

    public void eventTriggered(Player player) {
        player.addAction(new ShowTriggeredEvent(this));
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
