package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.players.Player;

public interface ChoiceActionCard {

    void actionChoiceMade(Player player, int choice);

    String getName();

}
