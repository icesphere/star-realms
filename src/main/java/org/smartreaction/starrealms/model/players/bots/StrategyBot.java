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

    public StrategyBot(BotStrategy strategy, GameService gameService, Player player, Game game) {
        this(strategy, gameService);

        setAuthority(player.getAuthority());
        setCombat(player.getCombat());
        setTrade(player.getTrade());

        getDeck().addAll(player.getDeck());
        getDiscard().addAll(player.getDiscard());
        getHand().addAll(player.getHand());
        getBases().addAll(player.getBases());
        getInPlay().addAll(player.getInPlay());
        getPlayed().addAll(player.getPlayed());
        getHeroes().addAll(player.getHeroes());
        getGambits().addAll(player.getGambits());

        shuffles = player.getShuffles();
        turn = player.getTurn();
        turns = player.getTurns();

        setGame(game);
    }

    @Override
    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }

    public BotStrategy getStrategy() {
        return strategy;
    }
}