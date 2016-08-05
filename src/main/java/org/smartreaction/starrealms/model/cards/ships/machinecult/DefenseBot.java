package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class DefenseBot extends Ship
{
    public DefenseBot()
    {
        name = "Defense Bot";
        addFaction(Faction.MACHINE_CULT);
        cost = 2;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 1 Combat; You may scrap a card in your hand or discard pile. If you control two or more bases, gain 8 Combat.";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(1);
        player.optionallyScrapCardFromHandOrDiscard();
        if (player.getBases().size() >= 2) {
            player.addCombat(8);
        }
    }
}
