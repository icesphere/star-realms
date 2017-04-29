package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.DoNotAttackBase;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.cards.ships.DoNotBuyCard;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;
import org.smartreaction.starrealms.model.players.bots.strategies.VelocityStrategy;
import org.smartreaction.starrealms.service.GameService;
import org.smartreaction.starrealms.service.SimulatorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class SimulatorBot extends BotPlayer {
    BotStrategy strategy = null;

    private SimulatorService simulatorService;

    public SimulatorBot(GameService gameService, SimulatorService simulatorService) {
        super(gameService);
        this.simulatorService = simulatorService;
    }

    @Override
    public void takeTurn() {
        useBestStrategy();
        super.takeTurn();
    }

    private void useBestStrategy() {
        logSimulationInfo("Simulator Bot determining best strategy");

        Map<BotStrategy, Float> results = simulatorService.simulateBestStrategy(getGame(), 250);

        float bestWinPercentage = 0;

        BotStrategy bestStrategy = null;

        List<BotStrategy> strategies = new ArrayList<>(results.keySet());

        List<BotStrategy> sortedStrategies = strategies.stream()
                .sorted((s1, s2) -> results.get(s2).compareTo(results.get(s1)))
                .collect(toList());

        for (BotStrategy strategy : sortedStrategies) {
            Float winPercentage = results.get(strategy);

            logSimulationInfo("Win percentage for " + strategy.getClass().getSimpleName() + ": " + winPercentage);

            if (winPercentage > bestWinPercentage) {
                bestStrategy = strategy;
                bestWinPercentage = winPercentage;
            }
        }

        if (bestStrategy == null) {
            logSimulationInfo("Best strategy was not found, using Velocity Strategy");
            bestStrategy = new VelocityStrategy();
        } else {
            logSimulationInfo("<b>Best strategy: " + bestStrategy.getClass().getSimpleName() + "</b>");
        }

        this.strategy = bestStrategy;
    }

    @Override
    public List<Card> getCardsToBuy() {
        ArrayList<Card> cardsToBuy = new ArrayList<>();

        Map<Card, Float> results = simulatorService.simulateBestCardToBuy(getGame(), 600);

        if (results.isEmpty()) {
            return cardsToBuy;
        }

        List<Card> cards = new ArrayList<>(results.keySet());

        List<Card> sortedCards = cards.stream()
                .sorted((c1, c2) -> results.get(c2).compareTo(results.get(c1)))
                .collect(toList());

        logSimulationInfo("Simulator Bot determining best card to buy");

        Card bestCardToBuy = displayWinPercentagesAndGetBestCard(results, sortedCards);

        if (bestCardToBuy == null) {
            logSimulationInfo("Best card to buy was not found, using default buy score");
            return super.getCardsToBuy();
        } else {
            logSimulationInfo("<b>Best card to buy: " + bestCardToBuy.getName() + "</b>");
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

    @Override
    public List<Card> chooseCardsToScrapInTradeRow(int cards) {
        //todo simulate
        return super.chooseCardsToScrapInTradeRow(cards);
    }

    @Override
    protected boolean shouldScrapCard(Card card) {
        logSimulationInfo("Simulator Bot determining whether or not to scrap " + card.getName());

        Map<Boolean, Float> scrapCardForBenefitResults = simulatorService.simulateScrapCardForBeneift(getGame(), 400, card);

        Float notScrappingWinPercentage = scrapCardForBenefitResults.get(false);
        Float scrappingWinPercentage = scrapCardForBenefitResults.get(true);

        logSimulationInfo("<b>Win percentage when not scrapping " + card.getName() + ": " + notScrappingWinPercentage + "</b>");
        logSimulationInfo("<b>Win percentage when scrapping " + card.getName() + ": " + scrappingWinPercentage + "</b>");

        return scrappingWinPercentage >= notScrappingWinPercentage;
    }

    @Override
    protected boolean shouldUseHero(Hero hero) {
        logSimulationInfo("Simulator Bot determining whether or not to use hero " + hero.getName());
        Map<Boolean, Float> useHeroResults = simulatorService.simulateUseHero(getGame(), 400, hero);

        Float notUseHeroWinPercentage = useHeroResults.get(false);
        Float useHeroWinPercentage = useHeroResults.get(true);

        logSimulationInfo("<b>Win percentage when not using hero " + hero.getName() + ": " + notUseHeroWinPercentage + "</b>");
        logSimulationInfo("<b>Win percentage when using hero " + hero.getName() + ": " + useHeroWinPercentage + "</b>");

        return useHeroWinPercentage >= notUseHeroWinPercentage;
    }

    @Override
    public int getChoice(ChoiceActionCard choiceActionCard, Choice[] choices) {
        logSimulationInfo("Simulator Bot determining best choice for " + choiceActionCard.getName());

        Map<Integer, Float> choiceResults = simulatorService.simulateBestChoice(getGame(), 400, choiceActionCard, choices);

        float bestWinPercentage = 0;

        Choice bestChoice = null;

        for (Choice choice : choices) {
            Float choiceWinPercent = choiceResults.get(choice.getChoiceNumber());
            logSimulationInfo("Win percentage for " + choice.getText() + ": " + choiceWinPercent);
            if (choiceWinPercent > bestWinPercentage) {
                bestChoice = choice;
                bestWinPercentage = choiceWinPercent;
            }
        }

        if (bestChoice == null) {
            logSimulationInfo("Best choice was not found, using default choice for strategy");
            return super.getChoice(choiceActionCard, choices);
        } else {
            logSimulationInfo("<b>Best choice: " + bestChoice.getText() + "</b>");
        }

        return bestChoice.getChoiceNumber();
    }

    @Override
    public void optionallyScrapCardsFromHandOrDiscard(int maxCardsToScrap) {
        Map<Card, Float> results = simulatorService.simulateBestCardToScrap(getGame(), 600, true, true);
        processOptionalScrapResults(maxCardsToScrap, results, true, true);
    }

    @Override
    public void optionallyScrapCardsFromDiscard(int maxCardsToScrap) {
        Map<Card, Float> results = simulatorService.simulateBestCardToScrap(getGame(), 600, true, false);
        processOptionalScrapResults(maxCardsToScrap, results, true, false);
    }

    @Override
    public void scrapCardFromHand(boolean optional) {
        if (optional) {
            Map<Card, Float> results = simulatorService.simulateBestCardToScrap(getGame(), 600, false, true);
            processOptionalScrapResults(1, results, false, true);
        } else {
            super.scrapCardFromHand(false);
        }
    }

    private void processOptionalScrapResults(int maxCardsToScrap, Map<Card, Float> results, boolean includeDiscard, boolean includeHand) {
        List<Card> cards = new ArrayList<>(results.keySet());

        List<Card> sortedCards = cards.stream()
                .sorted((c1, c2) -> results.get(c2).compareTo(results.get(c1)))
                .collect(toList());

        logSimulationInfo("Simulator Bot determining best card to scrap");

        Card bestCardToScrap = displayWinPercentagesAndGetBestCard(results, sortedCards);

        if (bestCardToScrap == null) {
            logSimulationInfo("Best card to scrap was not found, using default scrap logic");
            super.optionallyScrapCardsFromHandOrDiscard(maxCardsToScrap);
        } else {
            logSimulationInfo("<b>Best card to scrap: " + bestCardToScrap.getName() + "</b>");
        }

        if (bestCardToScrap != null && !(bestCardToScrap instanceof DoNotBuyCard)) {
            if (includeDiscard) {
                for (Card card : getDiscard()) {
                    if (bestCardToScrap.getName().equals(card.getName())) {
                        scrapCardFromDiscard(bestCardToScrap);
                        if (maxCardsToScrap > 1) {
                            optionallyScrapCardsFromHandOrDiscard(maxCardsToScrap - 1);
                        } else {
                            return;
                        }
                    }
                }
            }

            if (includeHand) {
                for (Card card : getHand()) {
                    if (bestCardToScrap.getName().equals(card.getName())) {
                        scrapCardFromHand(bestCardToScrap);
                        if (maxCardsToScrap > 1) {
                            optionallyScrapCardsFromHandOrDiscard(maxCardsToScrap - 1);
                        } else {
                            return;
                        }
                    }
                }
            }
        }
    }

    private Card displayWinPercentagesAndGetBestCard(Map<Card, Float> results, List<Card> sortedCards) {
        float bestWinPercentage = 0;
        Card bestCard = null;

        for (Card cardToScrap : sortedCards) {
            Float winPercentage = results.get(cardToScrap);

            if (winPercentage > 0) {
                logSimulationInfo("Win percentage for " + cardToScrap.getName() + ": " + winPercentage);
            }

            if (winPercentage > bestWinPercentage) {
                bestCard = cardToScrap;
                bestWinPercentage = winPercentage;
            }
        }
        return bestCard;
    }

    public BotStrategy getStrategy() {
        return strategy;
    }

    private void logSimulationInfo(String log) {
        getGame().addSimulationLog(log);
    }

    @Override
    public Base chooseOpponentBaseToDestroy() {
        if (getOpponent().getBases().size() == 1) {
            return getOpponent().getBases().get(0);
        }
        
        Map<Base, Float> results = simulatorService.simulateBestBaseToDestroy(getGame(), 500);

        return getBestBaseFromResults(results, "destroy");
    }

    private Base getBestBaseFromResults(Map<Base, Float> results, String actionType) {
        if (results == null || results.isEmpty()) {
            return null;
        }

        List<Base> bases = new ArrayList<>(results.keySet());

        List<Base> sortedBases = bases.stream()
                .sorted((b1, b2) -> results.get(b2).compareTo(results.get(b1)))
                .collect(toList());

        float bestWinPercentage = 0;

        Base bestBase = null;

        logSimulationInfo("Simulator Bot determining best base to " + actionType);

        for (Base base : sortedBases) {
            Float winPercentage = results.get(base);

            if (winPercentage > 0) {
                logSimulationInfo("Win percentage for " + actionType + "ing " + base.getName() + ": " + winPercentage);
            }

            if (winPercentage > bestWinPercentage) {
                bestBase = base;
                bestWinPercentage = winPercentage;
            }
        }

        if (bestBase == null) {
            logSimulationInfo("Best base to " + actionType + " was not found, using default " + actionType + " call");
            return super.chooseOpponentBaseToDestroy();
        } else {
            logSimulationInfo("<b>Best base to " + actionType + ": " + bestBase.getName() + "</b>");
        }

        if (!(bestBase instanceof DoNotAttackBase)) {
            return bestBase;
        }

        return null;
    }

    @Override
    protected void attackOpponentAndBases() {
        Base bestBaseToAttack = getBestBaseToAttack();

        while (bestBaseToAttack != null && getCombat() > 0 && !getOpponent().getOutposts().isEmpty()) {
            attackOpponentBase(bestBaseToAttack);
            bestBaseToAttack = getBestBaseToAttack();
        }

        if (getCombat() >= getOpponent().getAuthority() && getOpponent().getOutposts().isEmpty()) {
            attackOpponentWithRemainingCombat();
        }

        while (bestBaseToAttack != null && getCombat() > 0 && getOpponent().getOutposts().isEmpty() && !getOpponent().getBases().isEmpty()) {
            attackOpponentBase(bestBaseToAttack);
            bestBaseToAttack = getBestBaseToAttack();
        }

        if (getCombat() > 0 && getOpponent().getOutposts().isEmpty()) {
            attackOpponentWithRemainingCombat();
        }
    }

    private Base getBestBaseToAttack() {
        Map<Base, Float> results = simulatorService.simulateBestBaseToAttack(getGame(), 600);

        return getBestBaseFromResults(results, "attack");
    }
}
