package org.smartreaction.starrealms.model.cards.ships.machinecult;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.players.Player;

public class MechCruiser extends Ship implements AlliableCard
{
    public MechCruiser()
    {
        name = "Mech Cruiser";
        addFaction(Faction.MACHINE_CULT);
        cost = 5;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat; You may scrap a card in your hand or discard pile; Ally: Destroy target base";
        autoAlly = false;
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(6);
        player.optionallyScrapCardFromHandOrDiscard();
    }

    @Override
    public void cardAllied(Player player, Faction faction)
    {
        player.destroyTargetBase();
    }

    @Override
    public boolean isScrapper() {
        return true;
    }

    @Override
    public boolean isActionable(Player player, String cardLocation) {
        //noinspection SimplifiableIfStatement
        if (cardLocation.equals(CARD_LOCATION_PLAY_AREA) && player.getOpponent().getBases().size() == 0) {
            return false;
        }
        return super.isActionable(player, cardLocation);
    }
}
