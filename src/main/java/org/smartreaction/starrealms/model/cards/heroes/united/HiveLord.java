package org.smartreaction.starrealms.model.cards.heroes.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.players.Player;

public class HiveLord extends Hero {
    public HiveLord() {
        name = "Hive Lord";
        set = CardSet.UNITED_HEROES;
        cost = 5;
        text = "Buy: Until end of turn, you may use all of your Blob Ally abilities. Scrap any number of cards currently in the trade row; Scrap: Until end of turn, you may use all of your Blob Ally abilities. Add 2 Combat. Draw a card.";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.BLOB;
    }

    @Override
    public void heroAcquired(Player player) {
        player.blobAlliedUntilEndOfTurn();
        player.optionallyScrapCardsInTradeRow(player.getGame().getTradeRow().size());
    }

    @Override
    public void cardScrapped(Player player) {
        player.blobAlliedUntilEndOfTurn();
        player.drawCard();
    }
}
