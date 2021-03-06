package org.smartreaction.starrealms.model.cards.ships.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class BattleBlob extends Ship implements ScrappableCard, AlliableCard
{
    public BattleBlob()
    {
        name = "Battle Blob";
        addFaction(Faction.BLOB);
        cost = 6;
        set = CardSet.CORE;
        text = "Add 8 Combat; Ally: Draw a card; Scrap: Add 4 Combat";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(8);
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.drawCard();
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.addCombat(4);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 4;
    }
}
