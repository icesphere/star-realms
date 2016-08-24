package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;
import org.smartreaction.starrealms.model.players.bots.strategies.VelocityStrategy;
import org.smartreaction.starrealms.service.GameService;

import java.util.List;
import java.util.Map;

public class SimulatorBot extends BotPlayer {
    BotStrategy strategy = null;

    public SimulatorBot(GameService gameService) {
        super(gameService);
    }

    @Override
    public void takeTurn() {
        useBestStrategy();
        super.takeTurn();
    }

    private void useBestStrategy() {
        System.out.println("Determining best strategy");

        Map<BotStrategy, Float> results = gameService.simulateBestStrategy(getGame(), 50);

        float bestWinPercentage = 0;

        BotStrategy bestStrategy = null;

        for (BotStrategy strategy : results.keySet()) {
            Float winPercentage = results.get(strategy);
            if (winPercentage > bestWinPercentage) {
                bestStrategy = strategy;
                bestWinPercentage = winPercentage;
            }
        }

        if (bestStrategy == null) {
            System.out.println("Best strategy was null, using Velocity Strategy");
            bestStrategy = new VelocityStrategy();
        } else {
            System.out.println("Best strategy: " + bestStrategy.getClass().getSimpleName());
        }

        this.strategy = bestStrategy;
    }

    @Override
    public List<Card> getCardsToBuy() {
        return super.getCardsToBuy();
        //todo
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
