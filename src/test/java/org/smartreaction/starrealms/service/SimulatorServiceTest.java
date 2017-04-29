package org.smartreaction.starrealms.service;

import org.junit.Before;
import org.junit.Test;
import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.GameOptions;
import org.smartreaction.starrealms.model.players.bots.StrategyBot;
import org.smartreaction.starrealms.model.players.bots.strategies.AttackVelocityStrategy;
import org.smartreaction.starrealms.model.players.bots.strategies.VelocityStrategy;
import org.smartreaction.starrealms.model.simulator.SimulationResults;

public class SimulatorServiceTest {

    SimulatorService simulatorService = new SimulatorService();

    GameService gameService = new GameService();

    @Before
    public void setUp() {
        simulatorService.gameService = new GameService();
    }

    @Test
    public void testSimulation() {

        GameOptions gameOptions = new GameOptions();
        gameOptions.setIncludeCrisisBasesAndBattleships(true);
        gameOptions.setIncludeCrisisFleetsAndFortresses(true);
        gameOptions.setIncludeCrisisHeroes(true);

        StrategyBot player1 = new StrategyBot(new AttackVelocityStrategy(), gameService);
        StrategyBot player2 = new StrategyBot(new VelocityStrategy(), gameService);

        Game game = gameService.createGameForSimulation(gameOptions, player1, player2);

        Game copiedGame = game.copyGameForSimulation();

        simulatorService.setupPlayersForCopiedGame(game, copiedGame, ((StrategyBot) game.getCurrentPlayer().getOpponent()).getStrategy(), ((StrategyBot) game.getCurrentPlayer()).getStrategy());

        SimulationResults results = simulatorService.simulateGameToEnd(game, copiedGame, 10000, true, true);

        System.out.println("examine results");
    }

}