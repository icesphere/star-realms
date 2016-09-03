package org.smartreaction.starrealms.model.cards.heroes;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public abstract class Hero extends Card implements ScrappableCard {
    protected Hero() {
    }

    @Override
    public void cardPlayed(Player player) {
        //do nothing unless overridden
    }

    public abstract Faction getAlliedFaction();

    @Override
    public boolean isActionable(Player player, String cardLocation) {
        return false;
    }

    public void heroAcquired(Player player) {
        //do nothing unless overridden
    }
}
