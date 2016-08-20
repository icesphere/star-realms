package org.smartreaction.starrealms.model.cards;

import org.smartreaction.starrealms.model.players.Player;

public interface AlliableCard {
    void cardAllied(Player player, Faction faction);
}
