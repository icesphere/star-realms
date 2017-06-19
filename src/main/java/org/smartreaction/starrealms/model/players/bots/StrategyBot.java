package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.Player;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;
import org.smartreaction.starrealms.service.GameService;

public class StrategyBot extends BotPlayer {
    private BotStrategy strategy;

    public StrategyBot(BotStrategy strategy, GameService gameService) {
        super(gameService);
        this.strategy = strategy;
    }

    public StrategyBot(BotStrategy strategy, GameService gameService, Player player, Game game, boolean opponent, boolean resetOnly) {
        this(strategy, gameService);

        setGame(game);

        copyFromPlayerForSimulation(player, !opponent, resetOnly);
    }

    @Override
    public int getBuyCardScore(Card card) {
        int buyCardScore = strategy.getBuyCardScore(card, this);

        //todo move to strategy classes
        if (getOpponent() instanceof StrategyBot) {
            int opponentBuyCardScore = ((StrategyBot) getOpponent()).getStrategy().getBuyCardScore(card, getOpponent());

            int buyScoreDifference = opponentBuyCardScore - buyCardScore;
            if (buyScoreDifference >= 10 && !(card instanceof Explorer)) {
                buyCardScore += (buyScoreDifference / 2);
            }
        }

        int averageTradeRowCost = getGame().getAverageTradeRowCost();

        buyCardScore += strategy.getBuyScoreIncreaseForAverageTradeRowCost(averageTradeRowCost, card, getCurrentDeckNumber());

        return buyCardScore;
    }

    public BotStrategy getStrategy() {
        return strategy;
    }
}
