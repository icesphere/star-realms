package org.smartreaction.starrealms.service;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.GameOptions;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.DoNotAttackBase;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.cards.ships.*;
import org.smartreaction.starrealms.model.players.Player;
import org.smartreaction.starrealms.model.players.bots.SimulatorBot;
import org.smartreaction.starrealms.model.players.bots.StrategyBot;
import org.smartreaction.starrealms.model.players.bots.strategies.*;
import org.smartreaction.starrealms.model.simulator.SimulationResults;

import javax.ejb.Stateless;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Stateless
public class SimulatorService {

    GameService gameService;

    public BotStrategy determineStrategyBasedOnCards(List<Card> cards) {
        List<Card> nonStarterCards = cards.stream()
                .filter(c -> !(c instanceof Scout) && !(c instanceof Viper) && !(c instanceof Explorer))
                .collect(toList());

        double blobOrStarEmpirePercentage = getPercentageByType(nonStarterCards,
                c -> c.hasFaction(Faction.BLOB)
                        || c.hasFaction(Faction.STAR_EMPIRE)
                        || (c.isHero() && (((Hero) c).getAlliedFaction() == Faction.BLOB
                        || ((Hero) c).getAlliedFaction() == Faction.STAR_EMPIRE)));

        double economyPercentage = getPercentageByType(nonStarterCards, c -> c.getTradeWhenPlayed() >= 2);
        double scrapPercentage = getPercentageByType(nonStarterCards, Card::isScrapper);
        double basesPercentage = getPercentageByType(nonStarterCards, Card::isBase);

        boolean attackStrategy = (blobOrStarEmpirePercentage >= 60 && nonStarterCards.size() > 2) || (blobOrStarEmpirePercentage == 100 && nonStarterCards.size() > 1);

        boolean scrapStrategy = scrapPercentage >= 10;

        boolean economyStrategy = economyPercentage >= 75 && nonStarterCards.size() > 2;

        boolean defenseStrategy = (basesPercentage >= 60 && nonStarterCards.size() > 2) || (basesPercentage == 100 && nonStarterCards.size() > 1);

        if (scrapStrategy) {
            if (blobOrStarEmpirePercentage >= 50) {
                return new AttackVelocityStrategy();
            } else if (basesPercentage >= 50) {
                return new DefenseVelocityStrategy();
            } else {
                return new VelocityStrategy();
            }
        }

        if (attackStrategy) {
            return new AttackStrategy();
        }

        if (defenseStrategy) {
            return new DefenseStrategy();
        }

        if (economyStrategy) {
            return new EconomyStrategy();
        }

        return new VelocityStrategy();
    }

    private double getPercentageByType(List<Card> cards, Function<Card, Boolean> typeMatcher) {
        double count = cards.stream().filter(typeMatcher::apply).count();
        if (count == 0) {
            return 0;
        }
        return (count / cards.size()) * 100;
    }

    public Map<Boolean, Float> simulateScrapCardForBeneift(Game originalGame, int timesToSimulate, Card cardToScrapForBenefit) {
        Map<Boolean, Float> results = new HashMap<>(2);

        BotStrategy playerStrategy = ((SimulatorBot) originalGame.getCurrentPlayer()).getStrategy();
        BotStrategy opponentStrategy = determineStrategyBasedOnCards(originalGame.getCurrentPlayer().getOpponent().getAllCards());

        Game copiedGameNoScrap = originalGame.copyGameForSimulation();
        setupPlayersForCopiedGame(originalGame, copiedGameNoScrap, opponentStrategy, playerStrategy);

        Optional<Card> cardToNotScrap = copiedGameNoScrap.getCurrentPlayer().getInPlay()
                .stream()
                .filter(c -> c.getName().equals(cardToScrapForBenefit.getName()))
                .findFirst();

        if (cardToNotScrap.isPresent()) {
            copiedGameNoScrap.getCurrentPlayer().setCardToNotScrapThisTurn(cardToNotScrap.get());
            SimulationResults resultsForNoChange = simulateGameToEnd(copiedGameNoScrap, timesToSimulate);
            results.put(false, resultsForNoChange.getWinPercentage());
        } else {
            System.out.println("Error finding card to not scrap");
            results.put(true, 0f);
        }

        Game copiedGameWithCardScrapped = originalGame.copyGameForSimulation();
        setupPlayersForCopiedGame(originalGame, copiedGameWithCardScrapped, opponentStrategy, playerStrategy);

        Optional<Card> cardToScrap = copiedGameWithCardScrapped.getCurrentPlayer().getInPlay()
                .stream()
                .filter(c -> c.getName().equals(cardToScrapForBenefit.getName()))
                .findFirst();

        if (cardToScrap.isPresent()) {
            copiedGameWithCardScrapped.getCurrentPlayer().scrapCardInPlayForBenefit(cardToScrap.get());
            SimulationResults resultsWithCardScrapped = simulateGameToEnd(copiedGameWithCardScrapped, timesToSimulate);
            results.put(true, resultsWithCardScrapped.getWinPercentage());
        } else {
            System.out.println("Error finding card to scrap for benefit");
            results.put(true, 0f);
        }

        return results;
    }

    public Map<Boolean, Float> simulateUseHero(Game originalGame, int timesToSimulate, Hero hero) {
        Map<Boolean, Float> results = new HashMap<>(2);

        BotStrategy playerStrategy = ((SimulatorBot) originalGame.getCurrentPlayer()).getStrategy();
        BotStrategy opponentStrategy = determineStrategyBasedOnCards(originalGame.getCurrentPlayer().getOpponent().getAllCards());

        Game copiedGameNoHeroPlayed = originalGame.copyGameForSimulation();
        setupPlayersForCopiedGame(originalGame, copiedGameNoHeroPlayed, opponentStrategy, playerStrategy);

        Optional<Hero> heroToNotPlay = copiedGameNoHeroPlayed.getCurrentPlayer().getHeroes()
                .stream()
                .filter(h -> h.getName().equals(hero.getName()))
                .findFirst();

        if (heroToNotPlay.isPresent()) {
            copiedGameNoHeroPlayed.getCurrentPlayer().setHeroToNotPlayThisTurn(heroToNotPlay.get());
            SimulationResults resultsForNoChange = simulateGameToEnd(copiedGameNoHeroPlayed, timesToSimulate);
            results.put(false, resultsForNoChange.getWinPercentage());
        } else {
            System.out.println("Error finding hero to not play");
            results.put(true, 0f);
        }

        Game copiedGameWithHeroUsed = originalGame.copyGameForSimulation();
        setupPlayersForCopiedGame(originalGame, copiedGameWithHeroUsed, opponentStrategy, playerStrategy);

        Optional<Hero> heroToUse = copiedGameWithHeroUsed.getCurrentPlayer().getHeroes()
                .stream()
                .filter(h -> h.getName().equals(hero.getName()))
                .findFirst();

        if (heroToUse.isPresent()) {
            copiedGameWithHeroUsed.getCurrentPlayer().scrapCardInPlayForBenefit(heroToUse.get());
            SimulationResults resultsWithUseHero = simulateGameToEnd(copiedGameWithHeroUsed, timesToSimulate);
            results.put(true, resultsWithUseHero.getWinPercentage());
        } else {
            System.out.println("Error finding hero to use");
            results.put(true, 0f);
        }

        return results;
    }

    public Map<Integer, Float> simulateBestChoice(Game originalGame, int timesToSimulate, ChoiceActionCard choiceActionCard, Choice[] choices) {
        Map<Integer, Float> choiceResults = new HashMap<>(2);

        BotStrategy opponentStrategy = determineStrategyBasedOnCards(originalGame.getCurrentPlayer().getOpponent().getAllCards());

        Game copiedGame = originalGame.copyGameForSimulation();

        setupPlayersForCopiedGame(originalGame, copiedGame, opponentStrategy, ((SimulatorBot) originalGame.getCurrentPlayer()).getStrategy());

        for (Choice choice : choices) {
            choiceActionCard.actionChoiceMade(copiedGame.getCurrentPlayer(), choice.getChoiceNumber());

            SimulationResults results = simulateGameToEnd(copiedGame, timesToSimulate);

            choiceResults.put(choice.getChoiceNumber(), results.getWinPercentage());
        }

        return choiceResults;
    }

    public Map<Card, Float> simulateBestCardToBuy(Game originalGame, int timesToSimulate) {
        Game copiedGame = originalGame.copyGameForSimulation();

        BotStrategy opponentStrategy = determineStrategyBasedOnCards(originalGame.getCurrentPlayer().getOpponent().getAllCards());

        Map<Card, Float> cardResults = new LinkedHashMap<>();

        List<Card> cardsToBuy = originalGame.getTradeRow()
                .stream()
                .filter(c -> c.getCost() <= originalGame.getCurrentPlayer().getTrade())
                .collect(toList());

        if (originalGame.getCurrentPlayer().getTrade() >= 2) {
            cardsToBuy.add(new Explorer());
        }

        if (cardsToBuy.isEmpty()) {
            return cardResults;
        }

        cardsToBuy.add(new DoNotBuyCard());

        setupPlayersForCopiedGame(originalGame, copiedGame, opponentStrategy, ((SimulatorBot) originalGame.getCurrentPlayer()).getStrategy());

        for (Card cardToBuy : cardsToBuy) {

            copiedGame.getCurrentPlayer().setCardToBuyThisTurn(cardToBuy.copyCardForSimulation());

            SimulationResults results = simulateGameToEnd(copiedGame, timesToSimulate);

            cardResults.put(cardToBuy, results.getWinPercentage());
        }

        return cardResults;
    }

    public Map<Card, Float> simulateBestCardToScrap(Game originalGame, int timesToSimulate, boolean includeDiscard, boolean includeHand) {
        Game copiedGame = originalGame.copyGameForSimulation();

        BotStrategy opponentStrategy = determineStrategyBasedOnCards(originalGame.getCurrentPlayer().getOpponent().getAllCards());

        Map<Card, Float> cardResults = new LinkedHashMap<>();

        Integer minCostInDiscard = getMinCost(originalGame.getCurrentPlayer().getDiscard());

        Integer minCostInHand = getMinCost(originalGame.getCurrentPlayer().getHand());

        Set<String> cardNamesToSimulate = new HashSet<>();
        List<Card> cardsToSimulate = new ArrayList<>();

        boolean scrapCardFromDiscard = includeDiscard && minCostInDiscard != null && (minCostInHand == null || minCostInDiscard <= minCostInHand);

        if (scrapCardFromDiscard) {
            List<Card> cards = originalGame.getCurrentPlayer().getDiscard().stream().filter(c -> c.getCost() == minCostInDiscard).collect(toList());
            for (Card card : cards) {
                if (!cardNamesToSimulate.contains(card.getName())) {
                    cardsToSimulate.add(card);
                    cardNamesToSimulate.add(card.getName());
                }
            }
        } else if (includeHand && minCostInHand != null) {
            List<Card> cards = originalGame.getCurrentPlayer().getHand().stream().filter(c -> c.getCost() == minCostInHand).collect(toList());
            for (Card card : cards) {
                if (!cardNamesToSimulate.contains(card.getName())) {
                    cardsToSimulate.add(card);
                    cardNamesToSimulate.add(card.getName());
                }
            }
        }

        if (!cardsToSimulate.isEmpty()) {
            cardsToSimulate.add(new DoNotScrapCard());

            setupPlayersForCopiedGame(originalGame, copiedGame, opponentStrategy, ((SimulatorBot) originalGame.getCurrentPlayer()).getStrategy());

            for (Card card : cardsToSimulate) {

                if (scrapCardFromDiscard) {
                    copiedGame.getCurrentPlayer().setCardToScrapFromDiscard(card);
                } else {
                    copiedGame.getCurrentPlayer().setCardToScrapFromHand(card);
                }

                SimulationResults results = simulateGameToEnd(copiedGame, timesToSimulate);

                cardResults.put(card, results.getWinPercentage());
            }
        }

        return cardResults;
    }

    private Integer getMinCost(List<Card> cards) {
        Integer minCost = null;

        if (!cards.isEmpty()) {
            Card minCostCard = Collections.min(cards, Comparator.comparingInt(Card::getCost));
            minCost = minCostCard.getCost();
        }

        return minCost;
    }

    public Map<Base, Float> simulateBestBaseToDestroy(Game originalGame, int timesToSimulate) {
        return simulateBestBase(originalGame, timesToSimulate, false);
    }

    public Map<Base, Float> simulateBestBaseToAttack(Game originalGame, int timesToSimulate) {
        return simulateBestBase(originalGame, timesToSimulate, true);
    }

    public Map<Base, Float> simulateBestBase(Game originalGame, int timesToSimulate, boolean attack) {
        Game copiedGame = originalGame.copyGameForSimulation();

        Player opponent = originalGame.getCurrentPlayer().getOpponent();

        BotStrategy opponentStrategy = determineStrategyBasedOnCards(opponent.getAllCards());

        Map<Base, Float> cardResults = new LinkedHashMap<>();

        List<Base> bases = new ArrayList<>();

        if (!opponent.getOutposts().isEmpty()) {
            bases.addAll(opponent.getOutposts());
        } else if (!opponent.getBases().isEmpty()) {
            bases.addAll(opponent.getBases());
        }

        if (attack) {
            bases = bases
                    .stream()
                    .filter(b -> originalGame.getCurrentPlayer().getCombat() >= b.getShield()).collect(toList());
        }

        if (bases.isEmpty()) {
            return null;
        }

        if (attack) {
            bases.add(new DoNotAttackBase());
        }

        setupPlayersForCopiedGame(originalGame, copiedGame, opponentStrategy, ((SimulatorBot) originalGame.getCurrentPlayer()).getStrategy());
        
        for (Base base : bases) {
            if (attack) {
                copiedGame.getCurrentPlayer().setBaseToAttackThisTurn((Base) base.copyCardForSimulation());
            } else {
                copiedGame.getCurrentPlayer().setBaseToDestroyThisTurn((Base) base.copyCardForSimulation());
            }

            SimulationResults results = simulateGameToEnd(copiedGame, timesToSimulate);

            cardResults.put(base, results.getWinPercentage());
        }

        return cardResults;
    }

    public void setupPlayersForCopiedGame(Game originalGame, Game copiedGame, BotStrategy opponentStrategy, BotStrategy playerStrategy) {
        setupPlayersForCopiedGame(originalGame, copiedGame, opponentStrategy, playerStrategy, false);
    }

    private void setupPlayersForCopiedGame(Game originalGame, Game copiedGame, BotStrategy opponentStrategy, BotStrategy playerStrategy, boolean resetOnly) {
        StrategyBot strategyBot = new StrategyBot(playerStrategy, gameService, originalGame.getCurrentPlayer(), copiedGame, false, resetOnly);

        StrategyBot opponentBot = new StrategyBot(opponentStrategy, gameService, originalGame.getCurrentPlayer().getOpponent(), copiedGame, true, resetOnly);

        strategyBot.setOpponent(opponentBot);
        opponentBot.setOpponent(strategyBot);

        List<Player> players = new ArrayList<>();

        if (originalGame.getCurrentPlayer().isFirstPlayer()) {
            players.add(strategyBot);
            players.add(opponentBot);
        } else {
            players.add(opponentBot);
            players.add(strategyBot);
        }

        copiedGame.setPlayers(players);
    }

    public Map<BotStrategy, Float> simulateBestStrategy(Game originalGame, int timesToSimulate) {
        Game copiedGame = originalGame.copyGameForSimulation();

        BotStrategy opponentStrategy = determineStrategyBasedOnCards(originalGame.getCurrentPlayer().getOpponent().getAllCards());
        originalGame.addSimulationLog("<b>Opponent determined to be using " + opponentStrategy.getClass().getSimpleName() + "</b>");

        Map<BotStrategy, Float> strategyResults = new LinkedHashMap<>();

        List<BotStrategy> strategies = new ArrayList<>();

        strategies.add(new AttackStrategy());
        strategies.add(new AttackVelocityStrategy());
        strategies.add(new DefenseStrategy());
        strategies.add(new DefenseVelocityStrategy());
        strategies.add(new VelocityStrategy());

        for (BotStrategy strategy : strategies) {
            setupPlayersForCopiedGame(originalGame, copiedGame, opponentStrategy, strategy);

            SimulationResults results = simulateGameToEnd(copiedGame, timesToSimulate, false, false);

            strategyResults.put(strategy, results.getWinPercentage());
        }

        return strategyResults;
    }

    public SimulationResults simulateGameToEnd(Game copiedGame, int timesToSimulate) {
        return simulateGameToEnd(copiedGame, timesToSimulate, false, false);
    }

    public SimulationResults simulateGameToEnd(Game copiedGame, int timesToSimulate, boolean includeExtraSimulationInfo, boolean randomizeCardsAndFirstPlayerForSimulation) {

        SimulationResults results = new SimulationResults();

        boolean createdWinGameLog = false;
        boolean createdLossGameLog = false;

        int totalGamesCounted = 0;

        List<Game> games = new ArrayList<>(timesToSimulate);

        Map<String, Map<Integer, Integer>> averageAuthorityByPlayerByTurn = new HashMap<>();

        LinkedHashMap<String, Integer> playerWinDifferentialByCardsAtEndOfGame = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> opponentWinDifferentialByCardsAtEndOfGame = new LinkedHashMap<>();

        LinkedHashMap<String, Float> playerWinPercentageByFirstDeckCard = new LinkedHashMap<>();
        LinkedHashMap<String, Float> opponentWinPercentageByFirstDeckCard = new LinkedHashMap<>();

        Map<String, Integer> playerWinsByFirstDeckCard = new HashMap<>();
        Map<String, Integer> playerTotalGamesByFirstDeckCard = new HashMap<>();

        Map<String, Integer> opponentWinsByFirstDeckCard = new HashMap<>();
        Map<String, Integer> opponentTotalGamesByFirstDeckCard = new HashMap<>();

        LinkedHashMap<String, Float> playerWinPercentageBySecondDeckCard = new LinkedHashMap<>();
        LinkedHashMap<String, Float> opponentWinPercentageBySecondDeckCard = new LinkedHashMap<>();

        Map<String, Integer> playerWinsBySecondDeckCard = new HashMap<>();
        Map<String, Integer> playerTotalGamesBySecondDeckCard = new HashMap<>();

        Map<String, Integer> opponentWinsBySecondDeckCard = new HashMap<>();
        Map<String, Integer> opponentTotalGamesBySecondDeckCard = new HashMap<>();

        LinkedHashMap<String, Float> playerWinPercentageByNumScoutsFirstTwoHands = new LinkedHashMap<>();
        LinkedHashMap<String, Float> opponentWinPercentageByNumScoutsFirstTwoHands = new LinkedHashMap<>();

        Map<String, Integer> playerWinsByNumScoutsFirstTwoHands = new HashMap<>();
        Map<String, Integer> playerTotalGamesByNumScoutsFirstTwoHands = new HashMap<>();

        Map<String, Integer> opponentWinsByNumScoutsFirstTwoHands = new HashMap<>();
        Map<String, Integer> opponentTotalGamesByNumScoutsFirstTwoHands = new HashMap<>();

        float turnTotal = 0;

        int wins = 0;

        int firstPlayerWins = 0;

        Player player = copiedGame.getCurrentPlayer();
        player.setSimulationPlayerId(UUID.randomUUID().toString());
        averageAuthorityByPlayerByTurn.put(player.getSimulationPlayerId(), new HashMap<>());

        Player opponent = copiedGame.getCurrentPlayer().getOpponent();
        opponent.setSimulationPlayerId(UUID.randomUUID().toString());
        averageAuthorityByPlayerByTurn.put(opponent.getSimulationPlayerId(), new HashMap<>());

        boolean simulatingBestCardToBuy = player.getCardToBuyThisTurn() != null;

        Game gameToResetTo = copiedGame.copyGameForSimulation();

        setupPlayersForCopiedGame(copiedGame, gameToResetTo,
                ((StrategyBot) copiedGame.getCurrentPlayer().getOpponent()).getStrategy(),
                ((StrategyBot) copiedGame.getCurrentPlayer()).getStrategy(), false);

        Game copiedGameCopy = copiedGame.copyGameForSimulation();

        setupPlayersForCopiedGame(copiedGame, copiedGameCopy,
                ((StrategyBot) copiedGame.getCurrentPlayer().getOpponent()).getStrategy(),
                ((StrategyBot) copiedGame.getCurrentPlayer()).getStrategy(), true);

        for (int i = 0; i < timesToSimulate; i++) {
            boolean createGameLog = !createdWinGameLog || !createdLossGameLog;

            copiedGameCopy.resetTo(copiedGame);

            if (randomizeCardsAndFirstPlayerForSimulation) {
                for (Player copiedGameCopyPlayer : copiedGameCopy.getPlayers()) {
                    for (Player copiedGamePlayer : copiedGame.getPlayers()) {
                        if (copiedGameCopyPlayer.getSimulationPlayerId().equals(copiedGamePlayer.getSimulationPlayerId())) {
                            copiedGameCopyPlayer.copyFromPlayerForSimulation(copiedGamePlayer,
                                    copiedGame.getCurrentPlayer().getSimulationPlayerId().equals(copiedGameCopyPlayer.getSimulationPlayerId()),
                                    true);
                        }
                    }
                }

                Random random = new Random();
                boolean switchPlayers = random.nextBoolean();

                copiedGameCopy.getPlayers().get(0).setFirstPlayer(!switchPlayers);
                copiedGameCopy.getPlayers().get(1).setFirstPlayer(switchPlayers);

                if (switchPlayers) {
                    copiedGameCopy.setCurrentPlayerIndex(1);
                } else {
                    copiedGameCopy.setCurrentPlayerIndex(0);
                }

                for (Player p : copiedGameCopy.getPlayers()) {
                    p.getHand().clear();
                    p.getDeck().clear();
                    gameService.setupPlayerCards(p);
                }
            } else {
                for (int j = 0; j < gameToResetTo.getCurrentPlayer().getHand().size(); j++) {
                    copiedGame.getCurrentPlayer().getHand().get(j).resetTo(gameToResetTo.getCurrentPlayer().getHand().get(j));
                }

                for (Player copiedGameCopyPlayer : copiedGameCopy.getPlayers()) {
                    for (Player copiedGamePlayer : copiedGame.getPlayers()) {
                        if (copiedGameCopyPlayer.getSimulationPlayerId().equals(copiedGamePlayer.getSimulationPlayerId())) {
                            copiedGameCopyPlayer.copyFromPlayerForSimulation(copiedGamePlayer,
                                    copiedGame.getCurrentPlayer().getSimulationPlayerId().equals(copiedGameCopyPlayer.getSimulationPlayerId()),
                                    true);
                        }
                    }
                }
            }

            Game game = simulateGameToEnd(copiedGameCopy, createGameLog, randomizeCardsAndFirstPlayerForSimulation);
            if (game == null || (simulatingBestCardToBuy && !game.getWinner().isBoughtSpecifiedCardOnFirstTurn() && !game.getLoser().isBoughtSpecifiedCardOnFirstTurn())) {
                continue;
            }

            LinkedHashMap<String, Integer> winnerWinDifferentialMap;
            LinkedHashMap<String, Integer> loserWinDifferentialMap;

            Map<String, Integer> winnerFirstDeckWinsMap;
            Map<String, Integer> winnerFirstDeckTotalGamesMap;
            Map<String, Integer> loserFirstDeckTotalGamesMap;

            Map<String, Integer> winnerSecondDeckWinsMap;
            Map<String, Integer> winnerSecondDeckTotalGamesMap;
            Map<String, Integer> loserSecondDeckTotalGamesMap;

            Map<String, Integer> winnerNumScoutsFirstTwoHandsWinsMap;
            Map<String, Integer> winnerNumScoutsFirstTwoHandsTotalGamesMap;
            Map<String, Integer> loserNumScoutsFirstTwoHandsTotalGamesMap;

            if (game.getWinner().isFirstPlayer()) {
                firstPlayerWins++;
            }

            if (game.getWinner().equals(player)) {
                winnerWinDifferentialMap = playerWinDifferentialByCardsAtEndOfGame;
                loserWinDifferentialMap = opponentWinDifferentialByCardsAtEndOfGame;

                winnerFirstDeckWinsMap = playerWinsByFirstDeckCard;
                winnerFirstDeckTotalGamesMap = playerTotalGamesByFirstDeckCard;
                loserFirstDeckTotalGamesMap = opponentTotalGamesByFirstDeckCard;

                winnerSecondDeckWinsMap = playerWinsBySecondDeckCard;
                winnerSecondDeckTotalGamesMap = playerTotalGamesBySecondDeckCard;
                loserSecondDeckTotalGamesMap = opponentTotalGamesBySecondDeckCard;

                winnerNumScoutsFirstTwoHandsWinsMap = playerWinsByNumScoutsFirstTwoHands;
                winnerNumScoutsFirstTwoHandsTotalGamesMap = playerTotalGamesByNumScoutsFirstTwoHands;
                loserNumScoutsFirstTwoHandsTotalGamesMap = opponentTotalGamesByNumScoutsFirstTwoHands;

                wins++;
                if (createGameLog) {
                    if (!createdWinGameLog && game.isCreateGameLog()) {
                        results.setWinGameLog(game.getGameLog().toString());
                        createdWinGameLog = true;
                    }
                    game.setGameLog(null);
                }
            } else {
                winnerWinDifferentialMap = opponentWinDifferentialByCardsAtEndOfGame;
                loserWinDifferentialMap = playerWinDifferentialByCardsAtEndOfGame;

                winnerFirstDeckWinsMap = opponentWinsByFirstDeckCard;
                winnerFirstDeckTotalGamesMap = opponentTotalGamesByFirstDeckCard;
                loserFirstDeckTotalGamesMap = playerTotalGamesByFirstDeckCard;

                winnerSecondDeckWinsMap = opponentWinsBySecondDeckCard;
                winnerSecondDeckTotalGamesMap = opponentTotalGamesBySecondDeckCard;
                loserSecondDeckTotalGamesMap = playerTotalGamesBySecondDeckCard;

                winnerNumScoutsFirstTwoHandsWinsMap = opponentWinsByNumScoutsFirstTwoHands;
                winnerNumScoutsFirstTwoHandsTotalGamesMap = opponentTotalGamesByNumScoutsFirstTwoHands;
                loserNumScoutsFirstTwoHandsTotalGamesMap = playerTotalGamesByNumScoutsFirstTwoHands;

                if (!createdLossGameLog && game.isCreateGameLog()) {
                    results.setLossGameLog(game.getGameLog().toString());
                    createdLossGameLog = true;
                }
                game.setGameLog(null);
            }

            if (includeExtraSimulationInfo) {
                String winnerStartingScoutsSplit = "";
                winnerStartingScoutsSplit += game.getWinner().getScoutsInFirstHand() + "/" + game.getWinner().getScoutsInSecondHand();
                Integer winsByStartingScouts = winnerNumScoutsFirstTwoHandsWinsMap.get(winnerStartingScoutsSplit);
                if (winsByStartingScouts == null) {
                    winsByStartingScouts = 1;
                } else {
                    winsByStartingScouts++;
                }
                winnerNumScoutsFirstTwoHandsWinsMap.put(winnerStartingScoutsSplit, winsByStartingScouts);
                Integer winnerTotalGamesByStartingScouts = winnerNumScoutsFirstTwoHandsTotalGamesMap.get(winnerStartingScoutsSplit);
                if (winnerTotalGamesByStartingScouts == null) {
                    winnerTotalGamesByStartingScouts = 1;
                } else {
                    winnerTotalGamesByStartingScouts++;
                }
                winnerNumScoutsFirstTwoHandsTotalGamesMap.put(winnerStartingScoutsSplit, winnerTotalGamesByStartingScouts);

                String loserStartingScoutsSplit = "";
                loserStartingScoutsSplit += game.getLoser().getScoutsInFirstHand() + "/" + game.getLoser().getScoutsInSecondHand();
                Integer loserTotalGamesByStartingScouts = loserNumScoutsFirstTwoHandsTotalGamesMap.get(loserStartingScoutsSplit);
                if (loserTotalGamesByStartingScouts == null) {
                    loserTotalGamesByStartingScouts = 1;
                } else {
                    loserTotalGamesByStartingScouts++;
                }
                loserNumScoutsFirstTwoHandsTotalGamesMap.put(loserStartingScoutsSplit, loserTotalGamesByStartingScouts);

                game.getWinner().getAllCards().forEach(c -> {
                    if (!(c instanceof Scout || c instanceof Viper)) {
                        Integer winDifferential = winnerWinDifferentialMap.get(c.getName());
                        if (winDifferential == null) {
                            winDifferential = 1;
                        } else {
                            winDifferential++;
                        }
                        winnerWinDifferentialMap.put(c.getName(), winDifferential);
                    }
                });

                game.getLoser().getAllCards().forEach(c -> {
                    if (!(c instanceof Scout || c instanceof Viper)) {
                        Integer winDifferential = loserWinDifferentialMap.get(c.getName());
                        if (winDifferential == null) {
                            winDifferential = -1;
                        } else {
                            winDifferential--;
                        }
                        loserWinDifferentialMap.put(c.getName(), winDifferential);
                    }
                });

                if (game.getWinner().getCardsAcquiredByDeck().get(1) != null) {
                    game.getWinner().getCardsAcquiredByDeck().get(1).forEach(c -> {
                        Integer winsForCard = winnerFirstDeckWinsMap.get(c.getName());
                        if (winsForCard == null) {
                            winsForCard = 1;
                        } else {
                            winsForCard++;
                        }
                        winnerFirstDeckWinsMap.put(c.getName(), winsForCard);

                        Integer totalGamesForCard = winnerFirstDeckTotalGamesMap.get(c.getName());
                        if (totalGamesForCard == null) {
                            totalGamesForCard = 1;
                        } else {
                            totalGamesForCard++;
                        }
                        winnerFirstDeckTotalGamesMap.put(c.getName(), totalGamesForCard);
                    });
                }

                if (game.getLoser().getCardsAcquiredByDeck().get(1) != null) {
                    game.getLoser().getCardsAcquiredByDeck().get(1).forEach(c -> {
                        Integer totalGamesForCard = loserFirstDeckTotalGamesMap.get(c.getName());
                        if (totalGamesForCard == null) {
                            totalGamesForCard = 1;
                        } else {
                            totalGamesForCard++;
                        }
                        loserFirstDeckTotalGamesMap.put(c.getName(), totalGamesForCard);
                    });
                }

                if (game.getWinner().getCardsAcquiredByDeck().get(2) != null) {
                    game.getWinner().getCardsAcquiredByDeck().get(2).forEach(c -> {
                        Integer winsForCard = winnerSecondDeckWinsMap.get(c.getName());
                        if (winsForCard == null) {
                            winsForCard = 1;
                        } else {
                            winsForCard++;
                        }
                        winnerSecondDeckWinsMap.put(c.getName(), winsForCard);

                        Integer totalGamesForCard = winnerSecondDeckTotalGamesMap.get(c.getName());
                        if (totalGamesForCard == null) {
                            totalGamesForCard = 1;
                        } else {
                            totalGamesForCard++;
                        }
                        winnerSecondDeckTotalGamesMap.put(c.getName(), totalGamesForCard);
                    });
                }

                if (game.getLoser().getCardsAcquiredByDeck().get(2) != null) {
                    game.getLoser().getCardsAcquiredByDeck().get(2).forEach(c -> {
                        Integer totalGamesForCard = loserSecondDeckTotalGamesMap.get(c.getName());
                        if (totalGamesForCard == null) {
                            totalGamesForCard = 1;
                        } else {
                            totalGamesForCard++;
                        }
                        loserSecondDeckTotalGamesMap.put(c.getName(), totalGamesForCard);
                    });
                }
            }

            totalGamesCounted++;
            if (includeExtraSimulationInfo) {
                games.add(game);
            }
            turnTotal += game.getTurn();
        }

        if (includeExtraSimulationInfo) {
            for (Game game : games) {
                Map<String, TreeMap<Integer, Integer>> authorityByPlayerByTurn = game.getAuthorityByPlayerByTurn();
                for (String playerName : authorityByPlayerByTurn.keySet()) {
                    Map<Integer, Integer> authorityByTurn = authorityByPlayerByTurn.get(playerName);
                    Map<Integer, Integer> averageAuthorityByTurn = averageAuthorityByPlayerByTurn.get(playerName);

                    for (Integer turn : authorityByTurn.keySet()) {
                        Integer authority = averageAuthorityByTurn.get(turn);
                        if (authority == null) {
                            authority = 0;
                        }

                        authority += authorityByTurn.get(turn);

                        averageAuthorityByTurn.put(turn, authority);
                    }
                }
            }

            for (String playerName : averageAuthorityByPlayerByTurn.keySet()) {
                Map<Integer, Integer> averageAuthorityByTurn = averageAuthorityByPlayerByTurn.get(playerName);
                for (Integer turn : averageAuthorityByTurn.keySet()) {
                    Integer authority = averageAuthorityByTurn.get(turn);
                    authority = authority / games.size();
                    averageAuthorityByTurn.put(turn, authority);
                }
            }

            playerTotalGamesByFirstDeckCard.keySet().forEach(cardName -> {
                Integer totalGamesForCard = playerTotalGamesByFirstDeckCard.get(cardName);
                Integer winsForCard = playerWinsByFirstDeckCard.get(cardName);
                if (winsForCard == null) {
                    playerWinPercentageByFirstDeckCard.put(cardName, 0f);
                } else {
                    playerWinPercentageByFirstDeckCard.put(cardName, ((float) winsForCard / totalGamesForCard) * 100);
                }
            });

            opponentTotalGamesByFirstDeckCard.keySet().forEach(cardName -> {
                Integer totalGamesForCard = opponentTotalGamesByFirstDeckCard.get(cardName);
                Integer winsForCard = opponentWinsByFirstDeckCard.get(cardName);
                if (winsForCard == null) {
                    opponentWinPercentageByFirstDeckCard.put(cardName, 0f);
                } else {
                    opponentWinPercentageByFirstDeckCard.put(cardName, ((float) winsForCard / totalGamesForCard) * 100);
                }
            });

            playerTotalGamesBySecondDeckCard.keySet().forEach(cardName -> {
                Integer totalGamesForCard = playerTotalGamesBySecondDeckCard.get(cardName);
                Integer winsForCard = playerWinsBySecondDeckCard.get(cardName);
                if (winsForCard == null) {
                    playerWinPercentageBySecondDeckCard.put(cardName, 0f);
                } else {
                    playerWinPercentageBySecondDeckCard.put(cardName, ((float) winsForCard / totalGamesForCard) * 100);
                }
            });

            opponentTotalGamesBySecondDeckCard.keySet().forEach(cardName -> {
                Integer totalGamesForCard = opponentTotalGamesBySecondDeckCard.get(cardName);
                Integer winsForCard = opponentWinsBySecondDeckCard.get(cardName);
                if (winsForCard == null) {
                    opponentWinPercentageBySecondDeckCard.put(cardName, 0f);
                } else {
                    opponentWinPercentageBySecondDeckCard.put(cardName, ((float) winsForCard / totalGamesForCard) * 100);
                }
            });

            playerTotalGamesByNumScoutsFirstTwoHands.keySet().forEach(scoutSplit -> {
                Integer totalGamesForScoutSplit = playerTotalGamesByNumScoutsFirstTwoHands.get(scoutSplit);
                Integer winsForScoutSplit = playerWinsByNumScoutsFirstTwoHands.get(scoutSplit);
                if (winsForScoutSplit == null) {
                    playerWinPercentageByNumScoutsFirstTwoHands.put(scoutSplit, 0f);
                } else {
                    playerWinPercentageByNumScoutsFirstTwoHands.put(scoutSplit, ((float) winsForScoutSplit / totalGamesForScoutSplit) * 100);
                }
            });

            opponentTotalGamesByNumScoutsFirstTwoHands.keySet().forEach(scoutSplit -> {
                Integer totalGamesForScoutSplit = opponentTotalGamesByNumScoutsFirstTwoHands.get(scoutSplit);
                Integer winsForScoutSplit = opponentWinsByNumScoutsFirstTwoHands.get(scoutSplit);
                if (winsForScoutSplit == null) {
                    opponentWinPercentageByNumScoutsFirstTwoHands.put(scoutSplit, 0f);
                } else {
                    opponentWinPercentageByNumScoutsFirstTwoHands.put(scoutSplit, ((float) winsForScoutSplit / totalGamesForScoutSplit) * 100);
                }
            });
        }

        float winPercentage;
        if (totalGamesCounted > 0) {
            winPercentage = ((float) wins / totalGamesCounted) * 100;
        } else {
            winPercentage = 0;
        }


        float firstPlayerWinPercentage;
        if (totalGamesCounted > 0) {
            firstPlayerWinPercentage = ((float) firstPlayerWins / totalGamesCounted) * 100;
        } else {
            firstPlayerWinPercentage = 0;
        }

        results.setTotalGamesCounted(totalGamesCounted);
        results.setWinPercentage(winPercentage);
        results.setFirstPlayerWinPercentage(firstPlayerWinPercentage);
        results.setAverageNumTurns(turnTotal / totalGamesCounted);

        for (String playerName : averageAuthorityByPlayerByTurn.keySet()) {
            Map<Integer, Integer> averageAuthorityByTurn = averageAuthorityByPlayerByTurn.get(playerName);
            if (playerName.equals(player.getPlayerName())) {
                results.setPlayerAverageAuthorityByTurn(averageAuthorityByTurn);
            } else {
                results.setOpponentAverageAuthorityByTurn(averageAuthorityByTurn);
            }
        }

        if (includeExtraSimulationInfo) {
            results.setPlayerWinDifferentialByCardsAtEndOfGame(sortByValueDescending(playerWinDifferentialByCardsAtEndOfGame));
            results.setOpponentWinDifferentialByCardsAtEndOfGame(sortByValueDescending(opponentWinDifferentialByCardsAtEndOfGame));

            results.setPlayerWinPercentageByFirstDeckCard(sortByValueDescending(playerWinPercentageByFirstDeckCard));
            results.setOpponentWinPercentageByFirstDeckCard(sortByValueDescending(opponentWinPercentageByFirstDeckCard));

            results.setPlayerTotalGamesByFirstDeckCard(playerTotalGamesByFirstDeckCard);
            results.setOpponentTotalGamesByFirstDeckCard(opponentTotalGamesByFirstDeckCard);

            results.setPlayerWinPercentageBySecondDeckCard(sortByValueDescending(playerWinPercentageBySecondDeckCard));
            results.setOpponentWinPercentageBySecondDeckCard(sortByValueDescending(opponentWinPercentageBySecondDeckCard));

            results.setPlayerTotalGamesBySecondDeckCard(playerTotalGamesBySecondDeckCard);
            results.setOpponentTotalGamesBySecondDeckCard(opponentTotalGamesBySecondDeckCard);

            results.setPlayerWinPercentageByNumScoutsFirstTwoHands(sortByValueDescending(playerWinPercentageByNumScoutsFirstTwoHands));
            results.setOpponentWinPercentageByNumScoutsFirstTwoHands(sortByValueDescending(opponentWinPercentageByNumScoutsFirstTwoHands));

            results.setPlayerTotalGamesByNumScoutsFirstTwoHands(playerTotalGamesByNumScoutsFirstTwoHands);
            results.setOpponentTotalGamesByNumScoutsFirstTwoHands(opponentTotalGamesByNumScoutsFirstTwoHands);
        }

        return results;
    }

    private Game simulateGameToEnd(Game copiedGameCopy, boolean createGameLog, boolean randomizeTradeRow) {
        if (randomizeTradeRow) {
            copiedGameCopy.getDeck().addAll(copiedGameCopy.getTradeRow());
            copiedGameCopy.getTradeRow().clear();
        }

        Collections.shuffle(copiedGameCopy.getDeck());

        if (randomizeTradeRow) {
            copiedGameCopy.addCardsToTradeRow(5);
        }

        //copiedGameCopy.setCreateGameLog(createGameLog);

        copiedGameCopy.setupPlayerAuthorityMap();

        copiedGameCopy.getCurrentPlayer().takeTurn();

        while (!copiedGameCopy.isGameOver()) {
            if (copiedGameCopy.getTurn() > 100) {
                System.out.println("Turn over 100, stopping simulation");
                return null;
            } else {
                if (copiedGameCopy.isGameOver()) {
                    return copiedGameCopy;
                } else {
                    copiedGameCopy.getCurrentPlayer().takeTurn();
                }
            }
        }

        return copiedGameCopy;
    }

    private <S, T extends Comparable> LinkedHashMap<S, T> sortByValueDescending(LinkedHashMap<S, T> map) {
        LinkedHashMap<S, T> result = new LinkedHashMap<>();
        Stream<Map.Entry<S, T>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue((o1, o2) -> o2.compareTo(o1)))
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }
}
