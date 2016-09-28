package org.smartreaction.starrealms.model.cards.missions.united;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.cards.missions.PlayShipWhileBaseInPlay;
import org.smartreaction.starrealms.model.players.Player;

import java.util.List;

public class Convert extends Mission implements ChoiceActionCard {
    private List<Card> cardsRevealed;

    public Convert() {
        name = "Convert";
        objectiveText = "Play a Machine Cult ship while you have a Machine Cult base in play.";
        rewardText = "Reveal the top three cards of your deck. Put one in your hand, one in your discard pile, and one on top of your deck.";
    }

    @Override
    public boolean isMissionCompleted(Player player) {
        return PlayShipWhileBaseInPlay.isMissionCompleted(player, Faction.MACHINE_CULT);
    }

    @Override
    public void onMissionClaimed(Player player) {
        cardsRevealed = player.revealTopCardsOfDeck(3);

        if (cardsRevealed.size() == 3) {
            player.getDeck().removeAll(cardsRevealed);
            player.makeChoice(this, "Choose one to put in your hand",
                    new Choice(1, cardsRevealed.get(0).getName()),
                    new Choice(2, cardsRevealed.get(1).getName()),
                    new Choice(3, cardsRevealed.get(2).getName())
            );
        } else {
            //todo - what should happen if they don't have 3 cards to reveal?
        }
    }

    @Override
    public void actionChoiceMade(Player player, int choice) {
        Card card = cardsRevealed.remove(choice - 1);
        if (cardsRevealed.size() == 2) {
            player.addGameLog(player.getPlayerName() + " chose to add " + card.getName() + " to their hand");
            player.addCardToHand(card);

            player.makeChoice(this, "Choose one to put on top of your deck (the other card will go in your discard pile)",
                    new Choice(1, cardsRevealed.get(0).getName()),
                    new Choice(2, cardsRevealed.get(1).getName())
            );
        } else {
            player.addGameLog(player.getPlayerName() + " chose to put " + card.getName() + " on top of their deck");
            player.addCardToTopOfDeck(card);

            player.addGameLog(player.getPlayerName() + " chose to put " + card.getName() + " in their discard pile");
            player.addCardToDiscard(cardsRevealed.get(0));

        }
    }

    public List<Card> getCardsRevealed() {
        return cardsRevealed;
    }
}
