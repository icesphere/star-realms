package org.smartreaction.starrealms.model.cards.scenarios;

import org.smartreaction.starrealms.model.Game;

public abstract class OncePerTurnScenario extends Scenario {

    private String actionText;

    private boolean used;

    public OncePerTurnScenario(String name, String text, String actionText) {
        super(name, text);

        this.actionText = actionText;
    }

    public String getActionText() {
        return actionText;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public abstract boolean isActionAvailable(Game game);

    public abstract void processAction(Game game);
}
