package org.smartreaction.starrealms.model.cards.bases.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class ExchangePoint extends Base implements AlliableCard {
    public ExchangePoint() {
        name = "Exchange Point";
        addFaction(Faction.BLOB);
        addFaction(Faction.MACHINE_CULT);
        cost = 6;
        set = CardSet.UNITED_ASSAULT;
        shield = 7;
        text = "Add 2 Combat; Ally: Scrap a card in your hand, your discard pile, or the trade row";
        allFactionsAlliedTogether = true;
        autoAlly = false;
        autoUse = true;
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.optionallyScrapCardsFromHandOrDiscardOrTradeRow(1);
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
