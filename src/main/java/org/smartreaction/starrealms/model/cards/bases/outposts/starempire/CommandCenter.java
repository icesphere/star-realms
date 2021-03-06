package org.smartreaction.starrealms.model.cards.bases.outposts.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class CommandCenter extends Outpost
{
    public CommandCenter()
    {
        name = "Command Center";
        addFaction(Faction.STAR_EMPIRE);
        cost = 4;
        set = CardSet.COLONY_WARS;
        shield = 4;
        text = "Add 2 Trade; Whenever you play a Star Empire ship, gain 2 Combat";
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(2);
        player.setGainTwoCombatWhenStarEmpireShipPlayed(true);
    }

    @Override
    public void removedFromPlay(Player player) {
        player.setGainTwoCombatWhenStarEmpireShipPlayed(false);
    }

    @Override
    public int getTradeWhenPlayed() {
        return 2;
    }
}
