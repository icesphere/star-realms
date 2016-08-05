package org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class CapitolWorld extends Outpost
{
    public CapitolWorld()
    {
        name = "Capitol World";
        addFaction(Faction.TRADE_FEDERATION);
        cost = 8;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        shield = 6;
        text = "Add 6 Authority; Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addAuthority(6);
        player.drawCard();
    }
}
