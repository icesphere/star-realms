package org.smartreaction.starrealms.model.cards.ships.tradefederation;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class FrontierFerry extends Ship implements ScrappableCard
{
    public FrontierFerry()
    {
        name = "Frontier Ferry";
        faction = Faction.TRADE_FEDERATION;
        cost = 4;
        set = CardSet.COLONY_WARS;
        text = "Add 3 Trade; Add 4 Authority; Scrap: Destroy target base";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
        player.addAuthority(4);
    }

    @Override
    public void cardScrapped(Player player) {
        player.destroyTargetBase();
    }

    @Override
    public boolean canDestroyBasedWhenScrapped() {
        return true;
    }
}
