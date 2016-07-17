package org.smartreaction.starrealms.model.cards.bases.outposts.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class FortressOblivion extends Outpost
{
    public FortressOblivion()
    {
        name = "Fortress Oblivion";
        faction = Faction.MACHINE_CULT;
        cost = 3;
        set = CardSet.PROMO_YEAR_1;
        shield = 4;
        text = "If you played a base this turn (including this one), you may scrap a card in your hand or discard pile";
    }

    @Override
    public void cardPlayed(Player player) {
    }

    @Override
    public void baseUsed(Player player) {
        player.scrapCardFromHandOrDiscard();
    }

    @Override
    public boolean baseCanBeUsed(Player player) {
        return player.basePlayedThisTurn();
    }
}
