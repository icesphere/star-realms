package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class TheWrecker extends Ship implements AlliableCard
{
    public TheWrecker()
    {
        name = "The Wrecker";
        addFaction(Faction.MACHINE_CULT);
        cost = 7;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat; You may scrap up to two cards in your hand and/or discard pile; Ally: Draw a card";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.optionallyScrapCardsFromHandOrDiscard(2);
    }

    @Override
    public void cardAllied(Player player, Faction faction) {
        player.drawCard();
    }

    @Override
    public boolean isScrapper() {
        return true;
    }
}
