package org.smartreaction.starrealms.model.cards.heroes;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.players.Player;

public class WarElder extends Hero {
    public WarElder() {
        name = "War Elder";
        set = CardSet.CRISIS_HEROES;
        cost = 1;
        text = "Scrap: Until end of turn, you may use all of your Machine Cult Ally abilities. You may scrap a card from your hand";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.MACHINE_CULT;
    }

    @Override
    public void cardScrapped(Player player) {
        player.machineCultAlliedUntilEndOfTurn();
        player.scrapCardFromHand(true);
    }
}
