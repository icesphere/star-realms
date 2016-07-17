package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class StealthTower extends Outpost
{
    private Base baseBeingCopied;

    public StealthTower()
    {
        name = "Stealth Tower";
        faction = Faction.MACHINE_CULT;
        cost = 5;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Until your turn ends, Stealth Tower becomes a copy of any base in play. Stealth Tower has that base's faction in addition to Machine Cult.";
    }

    @Override
    public void baseUsed(Player player) {
        player.copyBase(this);
    }

    public Base getBaseBeingCopied() {
        return baseBeingCopied;
    }

    public void setBaseBeingCopied(Base baseBeingCopied) {
        this.baseBeingCopied = baseBeingCopied;
    }

    @Override
    public boolean isAlly(Card card) {
        return super.isAlly(card) || baseBeingCopied != null && super.isAlly(baseBeingCopied);
    }

    @Override
    public boolean isBlob() {
        return baseBeingCopied != null && baseBeingCopied.isBlob();
    }

    @Override
    public boolean isTradeFederation() {
        return baseBeingCopied != null && baseBeingCopied.isTradeFederation();
    }

    @Override
    public boolean isStarEmpire() {
        return baseBeingCopied != null && baseBeingCopied.isStarEmpire();
    }
}
