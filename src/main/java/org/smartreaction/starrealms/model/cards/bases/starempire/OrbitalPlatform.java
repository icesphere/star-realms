package org.smartreaction.starrealms.model.cards.bases.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class OrbitalPlatform extends Base
{
    public OrbitalPlatform()
    {
        name = "Orbital Platform";
        faction = Faction.STAR_EMPIRE;
        cost = 3;
        set = CardSet.COLONY_WARS;
        shield = 4;
        text = "Discard a card. If you do, draw a card.";
    }

    @Override
    public void baseUsed(Player player) {
        int cardsDiscarded = player.discardCards(1, false);
        if (cardsDiscarded > 0) {
            player.drawCard();
        }
    }
}
