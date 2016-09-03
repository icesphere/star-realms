package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.ships.DoNotBuyCard;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;
import org.smartreaction.starrealms.model.players.bots.strategies.VelocityStrategy;
import org.smartreaction.starrealms.service.GameService;

import java.util.ArrayList;
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
        getGame().gameLog("Simulator Bot determining best strategy");

        Map<BotStrategy, Float> results = gameService.simulateBestStrategy(getGame(), 50);

        float bestWinPercentage = 0;

        BotStrategy bestStrategy = null;

        for (BotStrategy strategy : results.keySet()) {
            Float winPercentage = results.get(strategy);

            getGame().gameLog("Win percentage for " + strategy.getClass().getSimpleName() + ": " + winPercentage);

            if (winPercentage > bestWinPercentage) {
                bestStrategy = strategy;
                bestWinPercentage = winPercentage;
            }
        }

        if (bestStrategy == null) {
            getGame().gameLog("Best strategy was not found, using Velocity Strategy");
            bestStrategy = new VelocityStrategy();
        } else {
            getGame().gameLog("Best strategy: " + bestStrategy.getClass().getSimpleName());
        }

        this.strategy = bestStrategy;
    }

    @Override
    public List<Card> getCardsToBuy() {
        ArrayList<Card> cardsToBuy = new ArrayList<>();

        Map<Card, Float> results = gameService.simulateBestCardToBuy(getGame(), 300);

        if (results.isEmpty()) {
            return cardsToBuy;
        }

        float bestWinPercentage = 0;

        Card bestCardToBuy = null;

        for (Card cardToBuy : results.keySet()) {
            Float winPercentage = results.get(cardToBuy);

            if (winPercentage > 0) {
                getGame().gameLog("Win percentage for " + cardToBuy.getName() + ": " + winPercentage);
            }

            if (winPercentage > bestWinPercentage) {
                bestCardToBuy = cardToBuy;
                bestWinPercentage = winPercentage;
            }
        }

        if (bestCardToBuy == null) {
            getGame().gameLog("Best card to buy was not found, using default buy score");
            return super.getCardsToBuy();
        } else {
            getGame().gameLog("Best card to buy: " + bestCardToBuy.getName());
        }

        if (!(bestCardToBuy instanceof DoNotBuyCard)) {
            cardsToBuy.add(bestCardToBuy);
        }

        return cardsToBuy;
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

    public BotStrategy getStrategy() {
        return strategy;
    }
}
