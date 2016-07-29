package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.gambits.Gambit;
import org.smartreaction.starrealms.model.players.BotPlayer;

public class EndGameBot extends BotPlayer {

    @Override
    public int getBuyCardScore(Card card) {
        //don't buy anything
        return 0;
    }

    @Override
    public int getScrapForBenefitScore(Card card) {
        //always scrap for benefit
        return 1;
    }

    @Override
    public int getUseGambitScore(Gambit gambit) {
        //use all gambits
        return 1;
    }
}
