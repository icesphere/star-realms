package org.smartreaction.starrealms.model.cards.heroes;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.players.Player;

public class BlobOverlord extends Hero {
    public BlobOverlord() {
        name = "Blob Overlord";
        set = CardSet.CRISIS_HEROES;
        cost = 2;
        text = "Scrap: Add 4 Combat. Until end of turn, you may use all of your Blob Ally abilities";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.BLOB;
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(4);
        player.blobAlliedUntilEndOfTurn();
    }

    @Override
    public int getCombatWhenScrapped() {
        return 4;
    }
}
