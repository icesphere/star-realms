package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.cards.Card;
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

        copyFromPlayerForSimulation(player, opponent, resetOnly);
    }

    @Override
    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }

    public BotStrategy getStrategy() {
        return strategy;
    }
}
