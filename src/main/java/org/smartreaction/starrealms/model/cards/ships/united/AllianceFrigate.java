package org.smartreaction.starrealms.model.cards.ships.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class AllianceFrigate extends Ship implements AlliableCard {
    public AllianceFrigate() {
        name = "Alliance Frigate";
        addFaction(Faction.TRADE_FEDERATION);
        addFaction(Faction.STAR_EMPIRE);
        cost = 3;
        set = CardSet.UNITED_COMMAND;
        text = "Add 4 Combat; Ally Star Empire: Add 3 Combat; Ally Trade Federation: Add 5 Authority";
        autoAlly = true;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(4);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        if (faction == Faction.TRADE_FEDERATION) {
            player.addAuthority(5);
        } else if (faction == Faction.STAR_EMPIRE) {
            player.addCombat(3);
        }
    }
}
