package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;
import org.smartreaction.starrealms.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class SimulatorBot extends BotPlayer {
    BotStrategy strategy = null;

    private GameService gameService;

    public SimulatorBot(GameService gameService) {
        super();
        this.gameService = gameService;
    }

    @Override
    public List<Card> getCardsToBuy() {
        //todo
        return new ArrayList<>();
        /*if (strategy != null) {
            return super.getCardsToBuy();
        }

        Map<Card, CardToBuySimulationResults> results = gameService.simulateBestCardToBuy(gameService.getGameStateFromGame(getGame()), 5);

        if (!results.isEmpty()) {
            Card bestCardToBuy = null;

            float bestWinPercentage = 0;

            for (Card card : results.keySet()) {
                float winPercentage = results.get(card).getWinPercentage();
                if (winPercentage > bestWinPercentage) {
                    bestCardToBuy = card;
                    bestWinPercentage = winPercentage;
                }
            }

            setStrategy(new VelocityStrategy());

            setCardToBuyThisTurn(bestCardToBuy);

            DecimalFormat f = new DecimalFormat("##.00");

            if (bestCardToBuy != null) {
                getGame().gameLog("Best card to buy this turn: " + bestCardToBuy.getName());
                getGame().gameLog("Win % with best card: " + f.format(bestWinPercentage) + "%");
            }
        }

        List<Card> cards = super.getCardsToBuy();
        setStrategy(null);
        return cards;*/
    }

    @Override
    public int getBuyCardScore(Card card) {
        if (card == null) {
            return 0;
        }
        if (strategy != null) {
            return strategy.getBuyCardScore(card, this);
        } else {
            return card.getCost() * card.getCost();
        }
    }

    public void setStrategy(BotStrategy strategy) {
        this.strategy = strategy;
    }
}
