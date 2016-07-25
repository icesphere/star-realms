package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.events.Event;
import org.smartreaction.starrealms.model.players.Player;

public class ShowTriggeredEvent extends Action {
    public ShowTriggeredEvent(Event event) {
        this.text = event.getName() + " event triggered: " + event.getText();
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return false;
    }

    @Override
    public boolean processAction(Player player) {
        return true;
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        return true;
    }

    @Override
    public String getDoneText() {
        return "Okay";
    }

    @Override
    public boolean isShowDone() {
        return true;
    }
}
