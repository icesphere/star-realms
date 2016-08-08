package org.smartreaction.starrealms.model.cards.heroes.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.players.Player;

public class ChancellorHartman extends Hero {
    public ChancellorHartman() {
        name = "Chancellor Hartman";
        set = CardSet.UNITED_HEROES;
        cost = 4;
        text = "Buy: Until end of turn, you may use all of your Machine Cult Ally abilities. You may scrap a card from your hand or discard pile; Scrap: Until end of turn, you may use all of your Machine Cult Ally abilities. You may scrap a card from your hand or discard pile.";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.MACHINE_CULT;
    }

    @Override
    public void heroBought(Player player) {
        player.machineCultAlliedUntilEndOfTurn();
        player.optionallyScrapCardFromHandOrDiscard();
    }

    @Override
    public void cardScrapped(Player player) {
        player.machineCultAlliedUntilEndOfTurn();
        player.optionallyScrapCardFromHandOrDiscard();
    }
}
