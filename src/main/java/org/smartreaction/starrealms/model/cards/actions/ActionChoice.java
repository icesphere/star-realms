package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.players.Player;

public interface ActionChoice {

    void actionChoiceMade(Player player, int choice);

}
