package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.CardCopier;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.cards.ships.DoNotBuyCard;
import org.smartreaction.starrealms.model.players.BotPlayer;
import org.smartreaction.starrealms.model.players.bots.strategies.BotStrategy;
import org.smartreaction.starrealms.model.players.bots.strategies.VelocityStrategy;
import org.smartreaction.starrealms.service.GameService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

    @Override
    public void endTurn() {
        super.endTurn();
    }

    private void useBestStrategy() {
        logSimulationInfo("Simulator Bot determining best strategy");

        Map<BotStrategy, Float> results = gameService.simulateBestStrategy(getGame(), 150);

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

        Map<Card, Float> results = gameService.simulateBestCardToBuy(getGame(), 600);

        if (results.isEmpty()) {
            return cardsToBuy;
        }

        List<Card> cards = new ArrayList<>(results.keySet());

        List<Card> sortedCards = cards.stream()
                .sorted((c1, c2) -> results.get(c2).compareTo(results.get(c1)))
                .collect(toList());

        float bestWinPercentage = 0;

        Card bestCardToBuy = null;

        logSimulationInfo("Simulator Bot determining best card to buy");

        for (Card cardToBuy : sortedCards) {
            Float winPercentage = results.get(cardToBuy);

            if (winPercentage > 0) {
                logSimulationInfo("Win percentage for " + cardToBuy.getName() + ": " + winPercentage);
            }

            if (winPercentage > bestWinPercentage) {
                bestCardToBuy = cardToBuy;
                bestWinPercentage = winPercentage;
            }
        }

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

        if (card instanceof CardCopier && ((CardCopier) card).getCardBeingCopied() != null) {
            card = ((CardCopier) card).getCardBeingCopied();
        }

        Map<Boolean, Float> scrapCardForBenefitResults = gameService.simulateScrapCardForBeneift(getGame(), 300, card);

        Float notScrappingWinPercentage = scrapCardForBenefitResults.get(false);
        Float scrappingWinPercentage = scrapCardForBenefitResults.get(true);

        logSimulationInfo("<b>Win percentage when not scrapping " + card.getName() + ": " + notScrappingWinPercentage + "</b>");
        logSimulationInfo("<b>Win percentage when scrapping " + card.getName() + ": " + scrappingWinPercentage + "</b>");

        return scrappingWinPercentage >= notScrappingWinPercentage;
    }

    @Override
    protected boolean shouldUseHero(Hero hero) {
        logSimulationInfo("Simulator Bot determining whether or not to use hero " + hero.getName());
        Map<Boolean, Float> useHeroResults = gameService.simulateUseHero(getGame(), 300, hero);

        Float notUseHeroWinPercentage = useHeroResults.get(false);
        Float useHeroWinPercentage = useHeroResults.get(true);

        logSimulationInfo("<b>Win percentage when not using hero " + hero.getName() + ": " + notUseHeroWinPercentage + "</b>");
        logSimulationInfo("<b>Win percentage when using hero " + hero.getName() + ": " + useHeroWinPercentage + "</b>");

        return useHeroWinPercentage >= notUseHeroWinPercentage;
    }

    @Override
    public int getChoice(ChoiceActionCard choiceActionCard, Choice[] choices) {
        Card card = (Card) choiceActionCard;

        logSimulationInfo("Simulator Bot determining best choice for " + card.getName());

        Map<Integer, Float> choiceResults = gameService.simulateBestChoice(getGame(), 300, choiceActionCard, choices);

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
    public List<List<Card>> getCardsToOptionallyScrapFromDiscardOrHand(int cards) {
        //todo
        return super.getCardsToOptionallyScrapFromDiscardOrHand(cards);
    }

    public BotStrategy getStrategy() {
        return strategy;
    }
    
    private void logSimulationInfo(String log) {
        getGame().addSimulationLog(log);
    }
}
