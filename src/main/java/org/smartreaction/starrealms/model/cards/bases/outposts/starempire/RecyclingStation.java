package org.smartreaction.starrealms.model.cards.bases.outposts.starempire;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.players.Player;

public class RecyclingStation extends Outpost
{
    public RecyclingStation()
    {
        name = "Recycling Station";
        faction = Faction.STAR_EMPIRE;
        cost = 4;
        set = CardSet.CORE;
        shield = 4;
        text = "Add 1 Trade OR Discard up to two cards, then draw that many cards";
    }

    @Override
    public void baseUsed(Player player) {
        Choice choice1 = new Choice(1, "Add 1 Trade");
        Choice choice2 = new Choice(2, "Discard up to two cards, then draw that many cards");
        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void choiceMade(int choice, Player player) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 1 Trade");
            player.addTrade(1);
        } else {
            player.getGame().gameLog("Chose Discard up to two cards, then draw that many cards");
            player.discardAndDrawCards(2);
        }
    }
}
