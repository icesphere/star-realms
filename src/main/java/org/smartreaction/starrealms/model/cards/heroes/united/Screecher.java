package org.smartreaction.starrealms.model.cards.heroes.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.players.Player;

public class Screecher extends Hero {
    public Screecher() {
        name = "Screecher";
        set = CardSet.UNITED_HEROES;
        cost = 3;
        text = "Buy: Until end of turn, you may use all of your Blob Ally abilities. Add 2 Combat. You may scrap a card in the trade row; Scrap: Until end of turn, you may use all of your Blob Ally abilities. Add 2 Combat. You may scrap a card in the trade row.";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.BLOB;
    }

    @Override
    public void heroBought(Player player) {
        player.blobAlliedUntilEndOfTurn();
        player.addCombat(2);
        player.optionalScrapCardInTradeRow();
    }

    @Override
    public void cardScrapped(Player player) {
        player.blobAlliedUntilEndOfTurn();
        player.addCombat(2);
        player.optionalScrapCardInTradeRow();
    }

    @Override
    public int getCombatWhenScrapped() {
        return 2;
    }
}
