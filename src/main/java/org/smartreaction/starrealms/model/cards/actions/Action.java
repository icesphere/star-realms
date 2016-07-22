package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public abstract class Action {
    protected String text;

    public String getText() {
        return text;
    }

    public List<Choice> getChoices() {
        return null;
    }

    public abstract boolean isCardActionable(Card card, String cardLocation, Player player);

    public abstract boolean processAction(Player player);

    public abstract boolean processActionResult(Player player, ActionResult result);

    public boolean isShowDoNotUse() {
        return false;
    }

    public boolean isShowDone() {
        return false;
    }

    public String getDoneText() {
        return "Done";
    }

    public boolean isCardSelected(Card card) {
        return false;
    }
}
