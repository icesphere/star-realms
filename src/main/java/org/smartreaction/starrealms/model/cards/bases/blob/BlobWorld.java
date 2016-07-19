package org.smartreaction.starrealms.model.cards.bases.blob;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.players.Player;

public class BlobWorld extends Base implements ChoiceActionCard
{
    public BlobWorld()
    {
        name = "Blob World";
        faction = Faction.BLOB;
        cost = 8;
        set = CardSet.CORE;
        shield = 7;
        text = "Add 5 Combat OR Draw a card for each Blob card that you've played this turn";
    }

    @Override
    public void baseUsed(Player player)
    {
        Choice choice1 = new Choice(1, "Add 5 Combat");
        Choice choice2 = new Choice(1, "Draw a card for each Blob card that you've played this turn");
        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 5 Combat");
            player.addCombat(5);
        } else {
            player.getGame().gameLog("Chose Draw a card for each Blob card that you've played this turn");
            int numBlobCardsPlayedThisTurn = player.getNumBlobCardsPlayedThisTurn();
            player.drawCards(numBlobCardsPlayedThisTurn);
        }
    }
}
