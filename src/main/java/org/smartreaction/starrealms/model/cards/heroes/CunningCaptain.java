package org.smartreaction.starrealms.model.cards.heroes;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.players.Player;

public class CunningCaptain extends Hero {
    public CunningCaptain() {
        name = "Cunning Captain";
        set = CardSet.CRISIS_HEROES;
        cost = 1;
        text = "Scrap: Until end of turn, you may use all of your Star Empire Ally abilities. Target player discards a card";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.STAR_EMPIRE;
    }

    @Override
    public void cardScrapped(Player player) {
        player.opponentDiscardsCard();
        player.starEmpireAlliedUntilEndOfTurn();
    }
}
