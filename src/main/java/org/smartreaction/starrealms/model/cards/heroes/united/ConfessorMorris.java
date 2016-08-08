package org.smartreaction.starrealms.model.cards.heroes.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.players.Player;

public class ConfessorMorris extends Hero {
    public ConfessorMorris() {
        name = "Confessor Morris";
        set = CardSet.UNITED_HEROES;
        cost = 5;
        text = "Buy: Until end of turn, you may use all of your Machine Cult Ally abilities. You may scrap up to two cards from your hand or discard pile; Scrap: Until end of turn, you may use all of your Machine Cult Ally abilities. Draw a card.";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.MACHINE_CULT;
    }

    @Override
    public void heroBought(Player player) {
        player.machineCultAlliedUntilEndOfTurn();
        player.optionallyScrapCardsFromHandOrDiscard(2);
    }

    @Override
    public void cardScrapped(Player player) {
        player.machineCultAlliedUntilEndOfTurn();
        player.drawCard();
    }
}
