package org.smartreaction.starrealms.model.players.bots;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.actions.ChoiceActionCard;
import org.smartreaction.starrealms.model.cards.bases.outposts.machinecult.StealthTower;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.cards.ships.DoNotBuyCard;
import org.smartreaction.starrealms.model.cards.ships.machinecult.StealthNeedle;
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
        addGameLog("Simulator Bot determining best strategy");

        Map<BotStrategy, Float> results = gameService.simulateBestStrategy(getGame(), 100);

        float bestWinPercentage = 0;

        BotStrategy bestStrategy = null;

        for (BotStrategy strategy : results.keySet()) {
            Float winPercentage = results.get(strategy);

            addGameLog("Win percentage for " + strategy.getClass().getSimpleName() + ": " + winPercentage);

            if (winPercentage > bestWinPercentage) {
                bestStrategy = strategy;
                bestWinPercentage = winPercentage;
            }
        }

        if (bestStrategy == null) {
            addGameLog("Best strategy was not found, using Velocity Strategy");
            bestStrategy = new VelocityStrategy();
        } else {
            addGameLog("Best strategy: " + bestStrategy.getClass().getSimpleName());
        }

        this.strategy = bestStrategy;
    }

    @Override
    public List<Card> getCardsToBuy() {
        ArrayList<Card> cardsToBuy = new ArrayList<>();

        Map<Card, Float> results = gameService.simulateBestCardToBuy(getGame(), 500);

        if (results.isEmpty()) {
            return cardsToBuy;
        }

        float bestWinPercentage = 0;

        Card bestCardToBuy = null;

        addGameLog("Simulator Bot determining best card to buy");

        for (Card cardToBuy : results.keySet()) {
            Float winPercentage = results.get(cardToBuy);

            if (winPercentage > 0) {
                addGameLog("Win percentage for " + cardToBuy.getName() + ": " + winPercentage);
            }

            if (winPercentage > bestWinPercentage) {
                bestCardToBuy = cardToBuy;
                bestWinPercentage = winPercentage;
            }
        }

        if (bestCardToBuy == null) {
            addGameLog("Best card to buy was not found, using default buy score");
            return super.getCardsToBuy();
        } else {
            addGameLog("Best card to buy: " + bestCardToBuy.getName());
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
        addGameLog("Simulator Bot determining whether or not to scrap " + card.getName());

        if (card instanceof StealthNeedle && ((StealthNeedle) card).getCardBeingCopied() != null) {
            card = ((StealthNeedle) card).getCardBeingCopied();
        } else if (card instanceof StealthTower && ((StealthTower) card).getCardBeingCopied() != null) {
            card = ((StealthTower) card).getCardBeingCopied();
        }

        Map<Boolean, Float> scrapCardForBenefitResults = gameService.simulateScrapCardForBeneift(getGame(), 200, card);

        Float notScrappingWinPercentage = scrapCardForBenefitResults.get(false);
        Float scrappingWinPercentage = scrapCardForBenefitResults.get(true);

        addGameLog("Win percentage when not scrapping " + card.getName() + ": " + notScrappingWinPercentage);
        addGameLog("Win percentage when scrapping " + card.getName() + ": " + scrappingWinPercentage);

        return scrappingWinPercentage > notScrappingWinPercentage || scrappingWinPercentage >= 100;
    }

    @Override
    protected boolean shouldUseHero(Hero hero) {
        addGameLog("Simulator Bot determining whether or not to use hero " + hero.getName());
        Map<Boolean, Float> useHeroResults = gameService.simulateUseHero(getGame(), 200, hero);

        Float notUseHeroWinPercentage = useHeroResults.get(false);
        Float useHeroWinPercentage = useHeroResults.get(true);

        addGameLog("Win percentage when not using hero" + hero.getName() + ": " + notUseHeroWinPercentage);
        addGameLog("Win percentage when using hero " + hero.getName() + ": " + useHeroWinPercentage);

        return useHeroWinPercentage > notUseHeroWinPercentage;
    }

    @Override
    public int getChoice(ChoiceActionCard choiceActionCard, Choice[] choices) {
        Card card = (Card) choiceActionCard;

        addGameLog("Simulator Bot determining best choice for " + card.getName());

        Map<Integer, Float> choiceResults = gameService.simulateBestChoice(getGame(), 100, choiceActionCard, choices);

        float bestWinPercentage = 0;
        
        Choice bestChoice = null;

        for (Choice choice : choices) {
            Float choiceWinPercent = choiceResults.get(choice.getChoiceNumber());
            addGameLog("Win percentage for " + choice.getText() + ": " + choiceWinPercent);
            if (choiceWinPercent > bestWinPercentage) {
                bestChoice = choice;
                bestWinPercentage = choiceWinPercent;
            }
        }

        if (bestChoice == null) {
            addGameLog("Best choice was not found, using default choice for strategy");
            return super.getChoice(choiceActionCard, choices);
        } else {
            addGameLog("Best choice: " + bestChoice.getText());
        }

        return bestChoice.getChoiceNumber();
    }

    public BotStrategy getStrategy() {
        return strategy;
    }
}
