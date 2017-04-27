package org.smartreaction.starrealms.model.players;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.actions.*;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.DoNotAttackBase;
import org.smartreaction.starrealms.model.cards.bases.blob.BlobWorld;
import org.smartreaction.starrealms.model.cards.bases.blob.DeathWorld;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.cards.bases.outposts.machinecult.*;
import org.smartreaction.starrealms.model.cards.bases.outposts.starempire.RecyclingStation;
import org.smartreaction.starrealms.model.cards.bases.outposts.starempire.SupplyDepot;
import org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation.DefenseCenter;
import org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation.PortOfCall;
import org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation.TradingPost;
import org.smartreaction.starrealms.model.cards.bases.outposts.united.CoalitionFortress;
import org.smartreaction.starrealms.model.cards.bases.starempire.FleetHQ;
import org.smartreaction.starrealms.model.cards.bases.tradefederation.BarterWorld;
import org.smartreaction.starrealms.model.cards.bases.tradefederation.Starmarket;
import org.smartreaction.starrealms.model.cards.events.BlackHole;
import org.smartreaction.starrealms.model.cards.events.Event;
import org.smartreaction.starrealms.model.cards.gambits.*;
import org.smartreaction.starrealms.model.cards.heroes.*;
import org.smartreaction.starrealms.model.cards.heroes.united.*;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.cards.missions.united.Convert;
import org.smartreaction.starrealms.model.cards.missions.united.Diversify;
import org.smartreaction.starrealms.model.cards.ships.*;
import org.smartreaction.starrealms.model.cards.ships.blob.Parasite;
import org.smartreaction.starrealms.model.cards.ships.machinecult.*;
import org.smartreaction.starrealms.model.cards.ships.starempire.*;
import org.smartreaction.starrealms.model.cards.ships.tradefederation.CustomsFrigate;
import org.smartreaction.starrealms.model.cards.ships.tradefederation.EmbassyYacht;
import org.smartreaction.starrealms.model.cards.ships.united.UnityFighter;
import org.smartreaction.starrealms.service.GameService;

import java.util.*;

import static java.util.stream.Collectors.toList;

public abstract class BotPlayer extends Player {
    public BotPlayer(GameService gameService) {
        this.gameService = gameService;
        playerName = getClass().getSimpleName();
    }

    protected GameService gameService;

    protected Comparator<Card> discardScoreDescending = (c1, c2) -> Integer.compare(getDiscardCardScore(c2), getDiscardCardScore(c1));
    protected Comparator<Card> scrapScoreDescending = (c1, c2) -> Integer.compare(getScrapCardScore(c2), getScrapCardScore(c1));
    protected Comparator<Card> scrapForBenefitScoreDescending = (c1, c2) -> Integer.compare(getScrapForBenefitScore(c2), getScrapForBenefitScore(c1));
    protected Comparator<Base> useBaseScoreDescending = (b1, b2) -> Integer.compare(getUseBaseScore(b2), getUseBaseScore(b1));
    protected Comparator<Card> playCardScoreDescending = (c1, c2) -> Integer.compare(getPlayCardScore(c2), getPlayCardScore(c1));
    protected Comparator<Card> cardToBuyScoreDescending = (c1, c2) -> Integer.compare(getBuyCardScore(c2), getBuyCardScore(c1));
    protected Comparator<Card> cardToBuyScoreAscending = cardToBuyScoreDescending.reversed();
    protected Comparator<Base> destroyBaseScoreDescending = (c1, c2) -> Integer.compare(getDestroyBaseScore(c2), getDestroyBaseScore(c1));
    protected Comparator<Base> destroyBaseScoreAscending = destroyBaseScoreDescending.reversed();
    protected Comparator<Base> attackBaseScoreDescending = (c1, c2) -> Integer.compare(getAttackBaseScore(c2), getAttackBaseScore(c1));
    protected Comparator<Card> copyShipScoreDescending = (c1, c2) -> Integer.compare(getCopyShipScore((Ship) c2), getCopyShipScore((Ship) c1));
    protected Comparator<Base> copyBaseScoreDescending = (b1, b2) -> Integer.compare(getCopyBaseScore(b2), getCopyBaseScore(b1));
    protected Comparator<Card> scrapCardFromTradeRowScoreDescending = (c1, c2) -> Integer.compare(getScrapCardFromTradeRowScore(c2), getScrapCardFromTradeRowScore(c1));
    protected Comparator<Card> cardToTopOfDeckScoreDescending = (c1, c2) -> Integer.compare(getCardToTopOfDeckScore(c2), getCardToTopOfDeckScore(c1));
    protected Comparator<Base> returnBaseToHandScoreDescending = (c1, c2) -> Integer.compare(getReturnBaseToHandScore(c2), getReturnBaseToHandScore(c1));
    protected Comparator<Gambit> useGambitScoreDescending = (g1, g2) -> Integer.compare(getUseGambitScore(g2), getUseGambitScore(g1));
    protected Comparator<Card> returnCardToTopOfDeckScoreDescending = (c1, c2) -> Integer.compare(getReturnCardToTopOfDeckScore(c2), getReturnCardToTopOfDeckScore(c1));
    protected Comparator<Hero> useHeroScoreDescending = (h1, h2) -> Integer.compare(getUseHeroScore(h2), getUseHeroScore(h1));

    public void takeTurn() {
        boolean endTurn = false;

        while (!endTurn) {
            endTurn = true;

            List<Base> unusedBasesAndOutposts = getUnusedBasesAndOutposts();

            endTurn = useBases(true, unusedBasesAndOutposts);

            List<Gambit> scrappableGambits = getScrappableGambits();
            if (!scrappableGambits.isEmpty()) {
                List<Gambit> sortedGambits = scrappableGambits.stream().sorted(useGambitScoreDescending).collect(toList());
                for (Gambit gambit : sortedGambits) {
                    if (getUseGambitScore(gambit) > 0) {
                        endTurn = false;
                        scrapCardInPlayForBenefit(gambit);
                    }
                }
            }

            if (!getHeroes().isEmpty()) {
                List<Hero> sortedHeroes = getHeroes().stream().sorted(useHeroScoreDescending).collect(toList());
                for (Hero hero : sortedHeroes) {
                    if (usingHeroHasPossibleBenefit(hero) && shouldUseHero(hero)) {
                        scrapCardInPlayForBenefit(hero);
                        endTurn = false;
                    }
                }

                if (!endTurn) {
                    refreshGamePageForOpponent();
                }
            }

            if (!getHand().isEmpty()) {
                endTurn = false;

                boolean doNotPlayVipers = getHand().stream().anyMatch(c -> !(c instanceof Viper));

                while (!getHand().isEmpty()) {
                    List<Card> sortedCards = getHand().stream()
                            .sorted(playCardScoreDescending).collect(toList());

                    if (doNotPlayVipers) {
                        sortedCards = sortedCards.stream().filter(c -> !(c instanceof Viper)).collect(toList());
                    }

                    if (sortedCards.isEmpty()) {
                        break;
                    }

                    Card card = sortedCards.get(0);
                    playCard(card);
                    if (card.isAlliableCard() && useAllyAfterPlay(card)) {
                        useAlliedAbilities(card);
                    }
                    if (card.isBase() && useBaseAfterPlay((Base) card)) {
                        ((Base) card).useBase(this);
                    }
                    if (cardToNotScrapThisTurn == null || !cardToNotScrapThisTurn.equals(card)) {
                        if (card.isScrappable() && shouldScrapCard(card)) {
                            this.scrapCardInPlayForBenefit(card);
                        }
                    }
                }
            }

            for (Card card : getInPlay()) {
                if (card.isAlliableCard()) {
                    if (useAlliedAbilities((Card) card.getAlliableCard())) {
                        endTurn = false;
                    }
                }
            }

            refreshGamePageForOpponent();

            if (!getHeroes().isEmpty()) {
                List<Hero> sortedHeroes = getHeroes().stream().sorted(useHeroScoreDescending).collect(toList());
                for (Hero hero : sortedHeroes) {
                    if (usingHeroHasPossibleBenefit(hero) && shouldUseHero(hero)) {
                        scrapCardInPlayForBenefit(hero);
                        endTurn = false;
                    }
                }
            }

            unusedBasesAndOutposts = getUnusedBasesAndOutposts();

            endTurn = useBases(endTurn, unusedBasesAndOutposts);

            for (Mission mission : getUnClaimedMissions()) {
                if (mission.isMissionCompleted(this)) {
                    claimMission(mission);
                    endTurn = false;
                }
            }

            if (getTrade() > 0) {
                List<Card> cardsToBuy = getCardsToBuy();
                if (!cardsToBuy.isEmpty()) {
                    endTurn = false;
                    for (Card card : cardsToBuy) {
                        this.buyCard(card);
                    }
                }
            }

            if (endTurn) {
                List<Card> cardsToScrapForBenefit = new ArrayList<>();

                for (Card card : getInPlay()) {
                    if (card.isScrappable()) {
                        if (cardToNotScrapThisTurn == null || !cardToNotScrapThisTurn.equals(card)) {
                            if (shouldScrapCard(card)) {
                                cardsToScrapForBenefit.add(card);
                            }
                        }
                    }
                }

                if (!cardsToScrapForBenefit.isEmpty()) {
                    endTurn = false;
                    List<Card> sortedCardsToScrapForBenefit = cardsToScrapForBenefit.stream().sorted(scrapForBenefitScoreDescending).collect(toList());
                    for (Card card : sortedCardsToScrapForBenefit) {
                        this.scrapCardInPlayForBenefit(card);
                    }
                }
            }

            if (!endTurn) {
                refreshGamePageForOpponent();
            }
        }

        applyCombatAndEndTurn();
    }

    private boolean useBases(boolean endTurn, List<Base> unusedBasesAndOutposts) {
        if (!unusedBasesAndOutposts.isEmpty()) {
            List<Base> sortedBases = unusedBasesAndOutposts.stream().sorted(useBaseScoreDescending).collect(toList());
            for (Base sortedBase : sortedBases) {
                if (getUseBaseScore(sortedBase) > 0) {
                    if (sortedBase.useBase(this)) {
                        endTurn = false;
                    }
                }
            }
        }
        return endTurn;
    }

    private boolean usingHeroHasPossibleBenefit(Hero hero) {
        //noinspection SimplifiableIfStatement
        if (heroToNotPlayThisTurn != null && hero.equals(heroToNotPlayThisTurn)) {
            return false;
        }

        return getUseHeroScore(hero) > 0
                || hero instanceof AdmiralRasmussen
                || hero instanceof CunningCaptain
                || hero instanceof CEOShaner
                || hero instanceof CommanderKlik
                || hero instanceof ConfessorMorris
                || hero instanceof HiveLord
                || hero instanceof Screecher;
    }

    protected boolean shouldUseHero(Hero hero) {
        return getUseHeroScore(hero) > 0;
    }

    @Override
    public void destroyOwnBase(DestroyOwnBaseActionCard card, String text) {
        Base base = chooseOwnBaseToDestroy(true);
        if (base == null) {
            card.onNotUsed(this);
        }
    }

    @Override
    public void addCardAction(CardActionCard card, String text) {
        ActionResult result = new ActionResult();
        if (card instanceof StealthTower) {
            Base baseToCopy = getBaseToCopy();
            result.setSelectedCard(baseToCopy);
            card.processCardActionResult(null, this, result);
        } else if (card instanceof StealthNeedle) {
            Ship shipToCopy = getShipToCopy();
            result.setSelectedCard(shipToCopy);
            card.processCardActionResult(null, this, result);
        }
    }

    @Override
    public void showTriggeredEvent(Event event) {
        //do nothing
    }

    @Override
    public void addCardFromDiscardToTopOfDeck(Integer maxCost) {
        Card card = chooseCardFromDiscardToAddToTopOfDeck();
        if (card != null) {
            getDiscard().remove(card);
            addCardToTopOfDeck(card);
        }
    }


    @Override
    public void destroyTargetBase() {
        Base base = chooseOpponentBaseToDestroy();
        if (base != null) {
            getOpponent().baseDestroyed(base);
        }
    }

    @Override
    public void optionallyScrapCardsFromHandOrDiscard(int cards) {
        List<List<Card>> cardsToOptionallyScrapFromDiscardOrHand = getCardsToOptionallyScrapFromDiscardOrHand(cards);
        List<Card> cardsToScrapFromDiscard = cardsToOptionallyScrapFromDiscardOrHand.get(0);
        List<Card> cardsToScrapFromHand = cardsToOptionallyScrapFromDiscardOrHand.get(1);

        cardsToScrapFromDiscard.forEach(this::scrapCardFromDiscard);
        cardsToScrapFromHand.forEach(this::scrapCardFromHand);
    }

    @Override
    public void optionallyScrapCardsFromHandOrDiscardForBenefit(ScrapCardsForBenefitActionCard card, int numCardsToScrap, String text) {
        if (card instanceof DeathWorld) {
            handleDeathWorld();
            return;
        } else {
            List<List<Card>> cards = getCardsToOptionallyScrapFromDiscardOrHand(numCardsToScrap);

            List<Card> cardsToScrapFromDiscard = cards.get(0);
            List<Card> cardsToScrapFromHand = cards.get(1);

            List<Card> cardsScrapped = new ArrayList<>(cardsToScrapFromDiscard);
            cardsScrapped.addAll(cardsToScrapFromHand);

            cardsToScrapFromDiscard.forEach(this::scrapCardFromDiscard);
            cardsToScrapFromHand.forEach(this::scrapCardFromHand);

            card.cardsScrapped(this, cardsScrapped);
        }
    }

    @Override
    public void optionallyScrapCardsFromHandOrDiscardOrTradeRow(int cards) {
        //todo consider trade row
        optionallyScrapCardsFromHandOrDiscard(cards);
    }

    @Override
    public void optionallyScrapCardsInTradeRow(int cards) {
        List<Card> cardsToScrapInTradeRow = chooseCardsToScrapInTradeRow(cards);
        cardsToScrapInTradeRow.forEach(this.getGame()::scrapCardFromTradeRow);
    }

    @Override
    public void discardCardsFromHand(int cards) {
        List<Card> cardsToDiscard = getCardsToDiscard(cards, false);
        cardsToDiscard.forEach(this::discardCardFromHand);
    }

    @Override
    public void optionallyDiscardCardsForBenefit(DiscardCardsForBenefitActionCard card, int numCardsToDiscard, String text) {
        boolean optionalDiscard = true;

        if (card instanceof BlackHole) {
            if (getAuthority() < 10) {
                optionalDiscard = false;
            }
        }

        List<Card> cardsToDiscard = getCardsToDiscard(numCardsToDiscard, optionalDiscard);

        cardsToDiscard.forEach(this::discardCardFromHand);

        if (!cardsToDiscard.isEmpty()) {
            card.cardsDiscarded(this, cardsToDiscard);
        } else {
            card.onChoseDoNotUse(this);
        }
    }

    @Override
    public void discardCardsForBenefit(DiscardCardsForBenefitActionCard card, int numCardsToDiscard, String text) {
        //todo better logic

        List<Card> cardsToDiscard = getCardsToDiscard(numCardsToDiscard, false);

        if (!cardsToDiscard.isEmpty()) {
            cardsToDiscard.forEach(this::discardCardFromHand);
            card.cardsDiscarded(this, cardsToDiscard);
        } else {
            card.onChoseDoNotUse(this);
        }
    }

    @Override
    public void makeChoice(ChoiceActionCard card, Choice... choices) {
        int choice = getChoice(card, choices);
        card.actionChoiceMade(this, choice);
    }

    @Override
    public void makeChoice(ChoiceActionCard card, String text, Choice... choices) {
        makeChoice(card, choices);
    }

    @Override
    public void scrapCardFromHand(boolean optional) {
        Card card = getCardToScrapFromHand(optional);
        if (card != null) {
            scrapCardFromHand(card);
        }
    }

    @Override
    public void optionallyScrapCardsFromDiscard(int cards) {
        //todo better logic

        List<List<Card>> cardsToScrap = getCardsToOptionallyScrapFromDiscardOrHand(cards);

        List<Card> cardsToScrapFromDiscard = cardsToScrap.get(0);

        cardsToScrapFromDiscard.forEach(this::scrapCardFromDiscard);
    }

    @Override
    public void returnTargetBaseToHand() {
        Base base = chooseBaseToReturnToHand();
        if (base != null) {
            base.setUsed(false);
            base.setAllAlliedAbilitiesToNotUsed();
            getBases().remove(base);
            addCardToHand(base);
        }
    }

    @Override
    public void acquireFreeCard(int maxCost) {
        Card card = chooseFreeCardToAcquire(maxCost, false, false);
        if (card != null) {
            if (!(card instanceof Explorer)) {
                getGame().getTradeRow().remove(card);
                getGame().addCardToTradeRow();
            }

            addGameLog(getPlayerName() + " acquired a free card from the trade row: " + card.getName());

            cardAcquired(card);
        }
    }

    @Override
    public void acquireFreeCardToTopOfDeck(int maxCost) {
        Card card = chooseFreeCardToAcquire(maxCost, false, false);
        if (card != null) {
            if (!(card instanceof Explorer)) {
                getGame().getTradeRow().remove(card);
                getGame().addCardToTradeRow();
            }

            addCardToTopOfDeck(card);
            cardAcquired(card);
        }
    }

    @Override
    public void acquireFreeCardToHand(int maxCost, boolean includeHeroes) {
        Card card = chooseFreeCardToAcquire(maxCost, false, includeHeroes);
        if (card != null) {
            if (!(card instanceof Explorer)) {
                getGame().getTradeRow().remove(card);
                getGame().addCardToTradeRow();
            }

            acquireCardToHand(card);
        }
    }

    @Override
    public void acquireFreeShipToTopOfDeck(Integer maxCost) {
        Card card = chooseFreeCardToAcquire(maxCost, true, false);
        if (card != null) {
            addGameLog(getPlayerName() + " acquired free " + card.getName() + " to top of deck");

            if (!(card instanceof Explorer)) {
                getGame().getTradeRow().remove(card);
                getGame().addCardToTradeRow();
            }

            acquireCardToTopOfDeck(card);
        }
    }

    public boolean useAllyAfterPlay(Card card) {
        if (card instanceof PatrolMech) {
            return true;
        }

        return false;
    }

    public boolean useBaseAfterPlay(Base base) {
        return base.isScrapper() || base instanceof RecyclingStation;
    }

    protected boolean shouldScrapCard(Card card) {
        return getScrapForBenefitScore(card) > 0;
    }

    public void applyCombatAndEndTurn() {
        attackOpponentAndBases();

        endTurn();

        refreshGamePageForOpponent();
    }

    protected void attackOpponentAndBases() {

        if (baseToAttackThisTurn != null) {
            for (Base base : getOpponent().getBases()) {
                if (getCombat() >= base.getShield() && base.getName().equals(baseToAttackThisTurn.getName())) {
                    attackOpponentBase(base);
                    baseToAttackThisTurn = null;
                    break;
                }
            }

            if (baseToAttackThisTurn instanceof DoNotAttackBase) {
                if (getCombat() > 0 && getOpponent().getOutposts().isEmpty()) {
                    attackOpponentWithRemainingCombat();
                }

                return;
            }
        }

        if (getCombat() > 0 && !getOpponent().getOutposts().isEmpty()) {
            List<Outpost> sortedOutposts = getOpponent().getOutposts().stream().sorted(attackBaseScoreDescending).collect(toList());
            for (Outpost outpost : sortedOutposts) {
                if (getAttackBaseScore(outpost) > 0 && getCombat() >= outpost.getShield()) {
                    attackOpponentBase(outpost);
                }
            }
        }

        if (getCombat() >= getOpponent().getAuthority() && getOpponent().getOutposts().isEmpty()) {
            attackOpponentWithRemainingCombat();
        }

        if (getCombat() > 0 && getOpponent().getOutposts().isEmpty() && !getOpponent().getBases().isEmpty()) {
            List<Base> sortedBases = getOpponent().getBases().stream().sorted(attackBaseScoreDescending).collect(toList());
            for (Base base : sortedBases) {
                if (getAttackBaseScore(base) > 0 && getCombat() >= base.getShield()) {
                    attackOpponentBase(base);
                }
            }
        }

        if (getCombat() > 0 && getOpponent().getOutposts().isEmpty()) {
            attackOpponentWithRemainingCombat();
        }
    }

    public List<Card> getCardsToBuy() {
        List<Card> cardsToBuy = new ArrayList<>();

        List<Card> cards = new ArrayList<>(getGame().getTradeRow());
        cards.add(getGame().getExplorer());
        cards.add(getGame().getExplorer());

        List<Card> sortedCards = cards.stream().filter(c -> getTrade() >= c.getCost()).sorted(cardToBuyScoreDescending).collect(toList());

        if (cardToBuyThisTurn != null && getTrade() >= cardToBuyThisTurn.getCost()) {
            if (cardToBuyThisTurn instanceof DoNotBuyCard) {
                return cardsToBuy;
            }
            cardsToBuy.add(cardToBuyThisTurn);
            return cardsToBuy;
        }

        if (!sortedCards.isEmpty() && getBuyCardScore(sortedCards.get(0)) > 0) {
            Card cardWithHighestBuyScore = sortedCards.get(0);

            if (sortedCards.size() > 2) {
                Map<Card, Integer> cardToBuyScoreMap = new HashMap<>();

                for (Card card : cards) {
                    if (!cardToBuyScoreMap.containsKey(card)) {
                        cardToBuyScoreMap.put(card, getBuyCardScore(card));
                    }
                }

                List<List<Card>> twoCardsList = new ArrayList<>();

                for (int i = 1; i < sortedCards.size() - 1; i++) {
                    Card cardToCompareAgainst = sortedCards.get(i);
                    for (int j = i + 1; j < sortedCards.size(); j++) {
                        if (addTwoCardListIfEnoughTrade(twoCardsList, cardToCompareAgainst, sortedCards.get(j))) {
                            break;
                        }
                    }
                }

                for (List<Card> cardList : twoCardsList) {
                    int totalBuyScore = 0;
                    totalBuyScore += cardToBuyScoreMap.get(cardList.get(0));
                    totalBuyScore += cardToBuyScoreMap.get(cardList.get(1));

                    if (totalBuyScore > cardToBuyScoreMap.get(cardWithHighestBuyScore)) {
                        return cardList;
                    }
                }
            }

            cardsToBuy.add(cardWithHighestBuyScore);
        }

        return cardsToBuy;
    }

    private boolean addTwoCardListIfEnoughTrade(List<List<Card>> twoCardList, Card card1, Card card2) {
        if ((card1.getCost() + card2.getCost()) <= getTrade()) {
            List<Card> cards = new ArrayList<>(2);
            cards.add(card1);
            cards.add(card2);
            twoCardList.add(cards);
            return true;
        }
        return false;
    }

    public int getBuyCardScore(Card card) {
        return card.getCost();
    }

    public int getUseBaseScore(Base card) {
        if (card instanceof RecyclingStation) {
            return 100;
        } else if (card instanceof MachineBase) {
            if (getAllCards().size() > 8) {
                return 90;
            } else {
                return 0;
            }
        } else if (card instanceof Junkyard) {
            return 80;
        } else if (card instanceof BrainWorld) {
            return 70;
        } else if (card instanceof FortressOblivion) {
            return 60;
        } else if (card instanceof BlobWorld) {
            return 10;
        } else {
            return 20;
        }
    }

    public int getPlayCardScore(Card card) {
        if (card instanceof FleetHQ) {
            return 100;
        } else if (card instanceof RecyclingStation) {
            return 90;
        } else if (card instanceof SupplyBot) {
            return 80;
        } else if (card instanceof TradeBot) {
            return 70;
        } else if (card instanceof MissileBot) {
            return 60;
        } else if (card instanceof PatrolMech) {
            return 50;
        } else if (card instanceof BrainWorld) {
            return 40;
        } else if (card instanceof Explorer) {
            return 10;
        } else if (card instanceof MegaMech) {
            return 7;
        } else if (card instanceof BattleBarge) {
            return 7;
        } else if (card instanceof EmbassyYacht) {
            return 6;
        } else if (card instanceof DefenseBot) {
            return 5;
        } else if (card instanceof StealthNeedle) {
            List<Card> cardsToCopy = new ArrayList<>(getPlayed());
            List<Card> otherCardsInHand = getHand().stream().filter(c -> !(c instanceof StealthNeedle)).collect(toList());
            cardsToCopy.addAll(otherCardsInHand);
            if (cardsToCopy.stream().allMatch(c -> c instanceof Scout || c instanceof Viper)) {
                return 1;
            } else {
                return 4;
            }
        } else if (card instanceof Scout) {
            return 3;
        } else if (card instanceof Viper) {
            return 2;
        } else {
            return 20;
        }
    }

    public int getDestroyBaseScore(Base card) {
        int opponentStarterCards = getOpponent().countCardsByType(getOpponent().getAllCards(), Card::isStarterCard);

        if (card instanceof BrainWorld) {
            if (opponentStarterCards >= 8) {
                return 12;
            } else if (opponentStarterCards >= 6) {
                return 11;
            } else if (opponentStarterCards >= 4) {
                return 10;
            }
        } else if (card instanceof MachineBase) {
            if (opponentStarterCards >= 8) {
                return 11;
            } else if (opponentStarterCards >= 6) {
                return 10;
            } else if (opponentStarterCards >= 4) {
                return 9;
            }
        } else if (card instanceof Junkyard) {
            if (opponentStarterCards >= 8) {
                return 8;
            } else if (opponentStarterCards >= 6) {
                return 7;
            }
        } else if (card instanceof BlobWorld) {
            int opponentBlobCards = getOpponent().countCardsByType(getOpponent().getAllCards(), c -> c.hasFaction(Faction.BLOB));
            if (opponentBlobCards >= 6) {
                return 10;
            } else if (opponentBlobCards >= 3) {
                return 9;
            }
        } else if (card instanceof RecyclingStation) {
            if (opponentStarterCards >= 8) {
                return 6;
            } else if (opponentStarterCards >= 5) {
                return 5;
            }
        }

        return card.getCost();
    }

    public int getAttackBaseScore(Base card) {
        return getDestroyBaseScore(card);
    }

    public int getCopyShipScore(Ship card) {
        if (card instanceof StealthNeedle) {
            return 0;
        }

        return getBuyCardScore(card) + 1;
    }

    public int getCopyBaseScore(Base base) {
        if (base instanceof StealthTower) {
            return 0;
        }
        return getBuyCardScore(base);
    }

    public int getScrapCardFromTradeRowScore(Card card) {
        //todo better logic

        Faction factionWithMostCards = getFactionWithMostCards();
        Faction opponentFactionWithMostCards = getOpponent().getFactionWithMostCards();

        if (opponentFactionWithMostCards != null) {
            if (factionWithMostCards != null && factionWithMostCards == opponentFactionWithMostCards && getTrade() >= card.getCost()) {
                return 0;
            }
            if (!card.getFactions().isEmpty() && card.getFactions().iterator().next() == opponentFactionWithMostCards) {
                return card.getCost();
            }
        }

        return 0;
    }

    public int getCardToTopOfDeckScore(Card card) {
        return getBuyCardScore(card);
    }

    public int getReturnBaseToHandScore(Base card) {
        return getBuyCardScore(card);
    }

    public int getDiscardCardScore(Card card) {
        //todo determine when discarding Explorer would not be good
        if (card instanceof Viper) {
            return 100;
        } else if (card instanceof Scout) {
            return 90;
        } else if (card instanceof Explorer) {
            return 80;
        }

        return 20 - card.getCost();
    }

    public int getScrapCardScore(Card card) {
        if (card instanceof Viper) {
            return 100;
        } else if (card instanceof Scout) {
            return 90;
        } else if (card instanceof Explorer) {
            return 80;
        }

        return 20 - card.getCost();
    }

    public int getScrapForBenefitScore(Card card) {
        int authority = getAuthority();
        int opponentAuthority = getOpponent().getAuthority();

        int numOpponentOutposts = getOpponent().getOutposts().size();

        int deck = getCurrentDeckNumber();

        if (numOpponentOutposts == 0 && getTotalCombatIfAllCardsInPlayScrapped() >= opponentAuthority) {
            return 10;
        }

        if (card.canDestroyBasedWhenScrapped()) {
            if (numOpponentOutposts > 0 && getCombat() >= opponentAuthority) {
                return 10;
            }
        }

        if (card instanceof ImperialFrigate || card instanceof CustomsFrigate) {
            if (opponentAuthority <= 10 || authority <= 10) {
                return 5;
            }
        }

        if (card instanceof PortOfCall) {
            if (authority >= 20 && opponentAuthority <= 10) {
                return 5;
            }
        }

        if (card instanceof SurveyShip || card instanceof Falcon) {
            if (getOpponent().getHand().size() <= 4 || opponentAuthority <= 10 || authority <= 10) {
                return 5;
            }
        }

        if (card.getTradeWhenScrapped() > 0) {
            int buyScoreIncrease = getBuyScoreIncrease(card.getTradeWhenScrapped());
            if ((deck < 3 && buyScoreIncrease >= 20) || buyScoreIncrease >= 40) {
                return 5;
            }
        }

        if (card instanceof AgingBattleship) {
            if (getHand().isEmpty() && (opponentAuthority <= 10 || authority <= 10 || (opponentAuthority <= 20 && canOnlyDestroyBaseWithExtraCombat(2)))) {
                return 5;
            }
        }

        if (card instanceof UnityFighter) {
            Card cardToScrapFromDiscard = getCardToScrapFromDiscard(true);
            if (cardToScrapFromDiscard instanceof Scout || cardToScrapFromDiscard instanceof Viper) {
                return 15;
            }
            Card cardToScrapFromHand = getCardToScrapFromHand(true);
            if (cardToScrapFromHand instanceof Viper) {
                return 10;
            } else if (cardToScrapFromHand instanceof Scout) {
                addTrade(-1);
                int buyScoreIncrease = getBuyScoreIncrease(1);
                addTrade(1);
                if (buyScoreIncrease < 20) {
                    return 5;
                }
            }
        }

        if (getHand().isEmpty() && card.canDestroyBasedWhenScrapped() && numOpponentOutposts > 0 && (opponentAuthority <= 10 || authority <= 10)) {
            return 5;
        }

        if (getHand().isEmpty() && getTotalCombatIfAllCardsInPlayScrapped() > 0 && canOnlyDestroyBaseWithExtraCombat(card.getCombatWhenScrapped())) {
            return 5;
        }

        if (card instanceof Explorer) {
            if (deck > 2) {
                return 5;
            }
        }

        if (card instanceof StealthTower && ((StealthTower) card).getCardBeingCopied() != null) {
            return getScrapForBenefitScore(((StealthTower) card).getCardBeingCopied());
        }

        return 0;
    }

    public int getReturnCardToTopOfDeckScore(Card card) {
        return 1000 - getBuyCardScore(card);
    }

    public int getUseHeroScore(Hero hero) {
        int authority = getAuthority();
        int opponentAuthority = getOpponent().getAuthority();

        int numOpponentOutposts = getOpponent().getOutposts().size();

        if (numOpponentOutposts == 0 && getTotalCombatIfAllCardsInPlayScrapped() >= opponentAuthority) {
            return 10;
        } else if (getUnusedBasesAndOutposts().isEmpty() && getCombat() < getSmallestOutpostShield() && (getCombat() + getTotalCombatIfAllCardsInPlayScrapped()) >= getSmallestOutpostShield()) {
            return 10;
        }

        if (authority <= 15 && hero.getAuthorityWhenScrapped() > 0) {
            return 5;
        }

        for (Card card : getInPlay()) {
            if (card.isAlliableCard() && card.hasFaction(hero.getAlliedFaction()) && !card.isAlliedAbilityUsed(hero.getAlliedFaction())) {
                return 15;
            }
        }

        if (hero.isScrapper()) {
            Card cardToScrapFromHand = getCardToScrapFromHand(true);
            List<List<Card>> cardsToScrap = getCardsToOptionallyScrapFromDiscardOrHand(2);
            if (hero instanceof WarElder && cardToScrapFromHand != null) {
                return 5;
            } else if ((hero instanceof HighPriestLyle || hero instanceof ChancellorHartman) && cardsToScrap.size() > 0) {
                return 5;
            } else if (hero instanceof ConfessorMorris && cardsToScrap.size() >= 2) {
                return 5;
            }
        }

        return 0;
    }

    public int getChoice(ChoiceActionCard card, Choice[] choices) {
        int deck = getCurrentDeckNumber();
        int opponentAuthority = getOpponent().getAuthority();

        if (card instanceof Convert) {
            Convert convert = (Convert) card;

            int bestChoice = 1;
            int bestBuyScore = 0;

            for (int i = 0; i < convert.getCardsRevealed().size(); i++) {
                Card c = convert.getCardsRevealed().get(i);
                int buyCardScore = getBuyCardScore(c);
                if (buyCardScore > bestBuyScore) {
                    bestBuyScore = buyCardScore;
                    bestChoice = i + 1;
                }
            }

            return bestChoice;
        } else if (card instanceof Diversify) {
            if (this.canOnlyDestroyBaseWithExtraCombat(2)) {
                return 2;
            }
            if (opponentAuthority <= 2) {
                return 2;
            }
            if (opponentAuthority < 10 && getAuthority() > 10) {
                return 2;
            }
            if (deck < 3 && getAuthority() > 10) {
                return 1;
            }
        } else if (card instanceof BarterWorld) {
            if (deck < 3 && getAuthority() > 10) {
                return 2;
            }
        } else if (card instanceof BlobWorld) {
            int blobCardsPlayed = countCardsByType(getPlayed(), c -> c.hasFaction(Faction.BLOB));
            if (blobCardsPlayed >= 2 && opponentAuthority > 5) {
                return 2;
            }
        } else if (card instanceof DefenseCenter) {
            if (this.canOnlyDestroyBaseWithExtraCombat(2)) {
                return 2;
            }
            if (opponentAuthority <= 2) {
                return 2;
            }
            if (opponentAuthority < 10 && getAuthority() > 10) {
                return 2;
            }
        } else if (card instanceof PatrolMech) {
            if (this.canOnlyDestroyBaseWithExtraCombat(2)) {
                return 2;
            }
            if (deck > 2) {
                return 2;
            }
        } else if (card instanceof RecyclingStation) {
            int buyScoreIncrease = getBuyScoreIncrease(1);
            if (deck <= 3 && buyScoreIncrease >= 20) {
                return 1;
            } else {
                return 2;
            }
        } else if (card instanceof TradingPost) {
            if (deck <= 2) {
                return 1;
            }
            int buyScoreIncrease = getBuyScoreIncrease(1);
            if (getAuthority() >= 20 && buyScoreIncrease >= 20) {
                return 1;
            } else {
                return 2;
            }
        } else if (card instanceof Starmarket) {
            if (deck <= 2) {
                return 2;
            }
        } else if (card instanceof BorderFort) {
            if (this.canOnlyDestroyBaseWithExtraCombat(2)) {
                return 2;
            }
            if (deck <= 2) {
                return 1;
            }
            if (deck > 3) {
                return 2;
            }
            int buyScoreIncrease = getBuyScoreIncrease(1);
            if (buyScoreIncrease >= 20) {
                return 1;
            } else {
                return 2;
            }
        } else if (card instanceof SupplyDepot) {
            if (this.canOnlyDestroyBaseWithExtraCombat(2)) {
                return 2;
            }
            if (deck <= 2) {
                return 1;
            }
            if (deck > 3) {
                return 2;
            }
            int buyScoreIncrease = getBuyScoreIncrease(2);
            if (buyScoreIncrease >= 20) {
                return 1;
            } else {
                return 2;
            }
        }  else if (card instanceof Parasite) {
            if (this.canOnlyDestroyBaseWithExtraCombat(2)) {
                return 1;
            }
            if (deck <= 2) {
                return 2;
            }
            if (deck > 3) {
                return 1;
            }
            if (getHighestBuyScoreForTrade(6) >= 50) {
                return 2;
            } else {
                return 1;
            }
        }  else if (card instanceof FrontierStation) {
            if (this.canOnlyDestroyBaseWithExtraCombat(3)) {
                return 2;
            }
            if (deck <= 2) {
                return 1;
            }
            if (deck > 3) {
                return 2;
            }
            int buyScoreIncrease = getBuyScoreIncrease(2);
            if (buyScoreIncrease >= 25) {
                return 1;
            } else {
                return 2;
            }
        } else if (card instanceof CoalitionFortress) {
            if (this.canOnlyDestroyBaseWithExtraCombat(2)) {
                return 1;
            }
            if (opponentAuthority <= 2) {
                return 1;
            }
            if (opponentAuthority < 10 && getAuthority() > 10) {
                return 1;
            }
            return 2;
        }

        return 1;
    }

    public List<Card> getCardsToDiscard(int cards, boolean optional) {
        List<Card> cardsToDiscard = new ArrayList<>();

        if (!getHand().isEmpty()) {
            if (cards > getHand().size()) {
                cards = getHand().size();
            }
            List<Card> sortedCards = getHand().stream().sorted(discardScoreDescending).collect(toList());
            for (int i = 0; i < cards; i++) {
                Card card = sortedCards.get(i);
                int score = getDiscardCardScore(card);
                if (getHand().isEmpty() || (optional && score < 20)) {
                    break;
                } else {
                    cardsToDiscard.add(card);
                }
            }
        }

        return cardsToDiscard;
    }

    public Ship getShipToCopy() {
        if (!getInPlay().isEmpty()) {
            List<Card> sortedCards = getInPlay().stream().filter(Card::isShip).sorted(copyShipScoreDescending).collect(toList());

            Ship shipToCopy = (Ship) sortedCards.get(0);

            if (getCopyShipScore(shipToCopy) > 0) {
                return shipToCopy;
            }
        }

        return null;
    }

    public Base getBaseToCopy() {
        List<Base> bases = new ArrayList<>(getBases());
        bases.addAll(getOpponent().getBases());

        if (!bases.isEmpty()) {
            List<Base> sortedCards = bases.stream().filter(Card::isBase).sorted(copyBaseScoreDescending).collect(toList());

            Base baseToCopy = sortedCards.get(0);

            if (getCopyBaseScore(baseToCopy) > 0) {
                return baseToCopy;
            }
        }

        return null;
    }

    public Card getCardToScrapFromHand(boolean optional) {
        if (!getHand().isEmpty()) {

            if (optional && cardToScrapFromHand != null) {
                for (Card card : getHand()) {
                    if (card.getName().equals(cardToScrapFromHand.getName())) {
                        cardToScrapFromHand = null;
                        return card;
                    }
                }

                cardToScrapFromHand = null;
            }

            List<Card> sortedCards = getHand().stream().sorted(scrapScoreDescending).collect(toList());
            Card card = sortedCards.get(0);
            if (optional && getScrapCardScore(card) < 20) {
                return null;
            }
            return card;
        }
        return null;
    }

    public Card getCardToScrapFromDiscard(boolean optional) {
        if (!getDiscard().isEmpty()) {
            List<Card> sortedCards = getDiscard().stream().sorted(scrapScoreDescending).collect(toList());
            Card card = sortedCards.get(0);
            if (optional && getScrapCardScore(card) < 20) {
                return null;
            }
            return card;
        }
        return null;
    }

    public Card chooseFreeShipToPutOnTopOfDeck() {
        List<Card> sortedCards = getGame().getTradeRow().stream().filter(Card::isShip).sorted(cardToTopOfDeckScoreDescending).collect(toList());

        if (!sortedCards.isEmpty()) {
            Card cardToTop = sortedCards.get(0);

            if (getCardToTopOfDeckScore(cardToTop) > 0) {
                return cardToTop;
            }
        }

        return null;
    }

    public List<Card> chooseCardsToScrapInTradeRow(int cards) {
        List<Card> sortedCards = getGame().getTradeRow().stream().sorted(scrapCardFromTradeRowScoreDescending).collect(toList());

        List<Card> cardsToScrap = new ArrayList<>();

        for (Card card : sortedCards) {
            if (cardsToScrap.size() >= cards || getScrapCardFromTradeRowScore(card) <= 0) {
                break;
            }
            cardsToScrap.add(card);
        }

        return cardsToScrap;
    }

    public List<List<Card>> getCardsToOptionallyScrapFromDiscardOrHand(int cards) {
        List<List<Card>> cardsToScrap = new ArrayList<>();

        List<Card> cardsToScrapFromDiscard = new ArrayList<>();
        List<Card> cardsToScrapFromHand = new ArrayList<>();

        cardsToScrap.add(cardsToScrapFromDiscard);
        cardsToScrap.add(cardsToScrapFromHand);

        if (cardToScrapFromDiscard != null) {
            for (Card card : getDiscard()) {
                if (card.getName().equals(cardToScrapFromDiscard.getName())) {
                    cardsToScrapFromDiscard.add(card);
                    break;
                }
            }

            cardToScrapFromDiscard = null;

            return cardsToScrap;
        }

        if (cardToScrapFromHand != null) {
            for (Card card : getHand()) {
                if (card.getName().equals(cardToScrapFromHand.getName())) {
                    cardsToScrapFromHand.add(card);
                    break;
                }
            }

            cardToScrapFromHand = null;

            return cardsToScrap;
        }

        if (!getDiscard().isEmpty()) {
            List<Card> sortedDiscardCards = getDiscard().stream().sorted(scrapScoreDescending).collect(toList());

            for (int i = 0; i < cards; i++) {
                if (sortedDiscardCards.size() <= i) {
                    break;
                }
                Card card = sortedDiscardCards.get(i);
                int score = getScrapCardScore(card);
                if (score < 20) {
                    break;
                } else {
                    cardsToScrapFromDiscard.add(card);
                    if(cardsToScrapFromDiscard.size() == cards) {
                        break;
                    }
                }
            }
        }

        if (!getHand().isEmpty() && cardsToScrapFromDiscard.size() < cards) {
            List<Card> sortedHandCards = getHand().stream().sorted(scrapScoreDescending).collect(toList());

            for (int i = 0; i < cards; i++) {
                if (sortedHandCards.size() <= i) {
                    break;
                }
                Card card = sortedHandCards.get(i);
                int score = getScrapCardScore(card);
                if (score < 20) {
                    break;
                } else {
                    cardsToScrapFromHand.add(card);
                    if (cardsToScrapFromDiscard.size() + cardsToScrapFromHand.size() == cards) {
                        break;
                    }
                }
            }
        }

        return cardsToScrap;
    }

    public Base chooseOpponentBaseToDestroy() {

        if (baseToDestroyThisTurn != null) {
            for (Base base : getOpponent().getBases()) {
                if (base.getName().equals(baseToDestroyThisTurn.getName())) {
                    baseToDestroyThisTurn = null;
                    return base;
                }
            }
        }

        if (!getOpponent().getOutposts().isEmpty()) {
            List<Base> sortedOutposts = getOpponent().getOutposts().stream().sorted(destroyBaseScoreDescending).collect(toList());
            Base baseToDestroy = sortedOutposts.get(0);
            if (getDestroyBaseScore(baseToDestroy) > 0) {
                return baseToDestroy;
            }
        } else if (!getOpponent().getBases().isEmpty()) {
            List<Base> sortedBases = getOpponent().getBases().stream().sorted(destroyBaseScoreDescending).collect(toList());
            Base baseToDestroy = sortedBases.get(0);
            if (getDestroyBaseScore(baseToDestroy) > 0) {
                return baseToDestroy;
            }
        }

        return null;
    }

    public Base chooseOwnBaseToDestroy(boolean optional) {
        Optional<Base> base = getBases().stream().sorted(destroyBaseScoreAscending).findFirst();
        if (base.isPresent()) {
            if (!optional || getDestroyBaseScore(base.get()) < 5) {
                return base.get();
            }
        }

        return null;
    }

    public int getBuyScoreIncrease(int extraTrade) {
        int cardToBuyScore = 0;

        List<Card> cardsToBuy = getCardsToBuy();
        if (!cardsToBuy.isEmpty()) {
            for (Card cardToBuy : cardsToBuy) {
                cardToBuyScore += getBuyCardScore(cardToBuy);
            }
        }

        List<Card> sortedCards = getGame().getTradeRow().stream().filter(c -> getTrade() + extraTrade >= c.getCost()).sorted(cardToBuyScoreDescending).collect(toList());
        if (!sortedCards.isEmpty()) {
            int bestCardScore = getBuyCardScore(sortedCards.get(0));
            return bestCardScore - cardToBuyScore;
        }

        return 0;
    }

    public int getHighestBuyScoreForTrade(int trade) {
        List<Card> sortedCards = getGame().getTradeRow().stream().filter(c -> trade >= c.getCost()).sorted(cardToBuyScoreDescending).collect(toList());
        if (!sortedCards.isEmpty()) {
            return getBuyCardScore(sortedCards.get(0));
        }

        return 0;
    }

    public Base chooseBaseToReturnToHand() {
        //todo include opponent's bases

        List<Base> sortedBases = getBases().stream().sorted(returnBaseToHandScoreDescending).collect(toList());

        if (!sortedBases.isEmpty()) {
            Base baseToReturnToHand = sortedBases.get(0);

            if (getReturnBaseToHandScore(baseToReturnToHand) > 0) {
                return baseToReturnToHand;
            }
        }

        return null;
    }

    public Faction chooseFactionForCard(Card card) {
        //todo create rules

        List<Card> cards = new ArrayList<>(getInPlay());
        cards.addAll(getHand());

        Map<Faction, Integer> factionCounts = new HashMap<>(4);

        factionCounts.put(Faction.BLOB, countCardsByType(cards, c -> c.hasFaction(Faction.BLOB)));
        factionCounts.put(Faction.STAR_EMPIRE, countCardsByType(cards, c -> c.hasFaction(Faction.STAR_EMPIRE)));
        factionCounts.put(Faction.TRADE_FEDERATION, countCardsByType(cards, c -> c.hasFaction(Faction.TRADE_FEDERATION)));
        factionCounts.put(Faction.MACHINE_CULT, countCardsByType(cards, c -> c.hasFaction(Faction.MACHINE_CULT)));

        Faction factionWithLeastCards = null;
        int lowestFactionCount = 100;

        for (Faction faction : factionCounts.keySet()) {
            if (factionCounts.get(faction) <= lowestFactionCount) {
                factionWithLeastCards = faction;
            }
        }

        return factionWithLeastCards;
    }

    public Card chooseCardFromDiscardToAddToTopOfDeck() {
        if (getDiscard().isEmpty()) {
            return null;
        }

        List<Card> sortedCards = getDiscard().stream().sorted(cardToBuyScoreDescending).collect(toList());

        Card card = sortedCards.get(0);
        if (getBuyCardScore(card) > 0) {
            return card;
        }

        return null;
    }

    public Card chooseFreeCardToAcquire(Integer maxCost, boolean onlyShips, boolean includeHeroes) {
        List<Card> cardsToChooseFrom = new ArrayList<>(getGame().getTradeRow());
        cardsToChooseFrom.add(getGame().getExplorer());

        List<Card> sortedCards = cardsToChooseFrom.stream()
                .filter(c -> (maxCost == null || c.getCost() <= maxCost)
                        && (includeHeroes || c.isShip() || c.isBase())
                        && (!onlyShips || c.isShip()))
                .sorted(cardToBuyScoreDescending).collect(toList());

        if (!sortedCards.isEmpty()) {
            Card card = sortedCards.get(0);
            if (getBuyCardScore(card) > 0) {
                return card;
            }
        }

        return null;
    }

    public int getUseGambitScore(Gambit gambit) {
        int starterCardsInPlay = countCardsByType(getInPlay(), Card::isStarterCard);

        if (gambit instanceof BoldRaid) {
            if (!getOpponent().getBases().isEmpty()) {
                return 12;
            } else if (starterCardsInPlay >= 3 && getHand().size() == 0 && getDeck().size() == 0) {
                return 4;
            }
        } else if (gambit instanceof EnergyShield) {
            if (starterCardsInPlay >= 3 && getHand().size() == 0 && getDeck().size() == 0) {
                return 6;
            }
        } else if (gambit instanceof PoliticalManeuver) {
            if (getHand().isEmpty() && getBuyScoreIncrease(2) >= 30) {
                return 2;
            }
        } else if (gambit instanceof RiseToPower) {
            if (starterCardsInPlay >= 3 && getHand().size() == 0 && getDeck().size() == 0) {
                return 18;
            } else if (getBuyScoreIncrease(1) >= 20) {
                return 14;
            }
        } else if (gambit instanceof SalvageOperation) {
            if (!getDiscard().isEmpty()) {
                List<Card> sortedCards = getDiscard().stream().sorted(cardToBuyScoreDescending).collect(toList());
                if (getBuyCardScore(sortedCards.get(0)) >= 30) {
                    return 24;
                }
            }
        } else if (gambit instanceof SmugglingRun) {
            List<Card> sortedCards = getGame().getTradeRow().stream().filter(c -> c.getCost() <= 4).sorted(cardToBuyScoreDescending).collect(toList());
            if (!sortedCards.isEmpty()) {
                if (getBuyCardScore(sortedCards.get(0)) >= 30) {
                    return 22;
                }
            }
        } else if (gambit instanceof SurpriseAssault) {
            if (getHand().isEmpty()) {
                if (getOpponent().getAuthority() <= 8) {
                    return 16;
                } else if (getCombat() < getOpponent().getBiggestOutpostShield()) {
                    return 1;
                } else if (getCombat() < getOpponent().getBiggestBaseShield()) {
                    return 1;
                }
            }
        } else if (gambit instanceof UnlikelyAlliance) {
            if (starterCardsInPlay >= 3 && getHand().size() == 0 && getDeck().size() <= 1) {
                return 20;
            } else if (getGame().getTurn() >= 5) {
                return 8;
            }
        }

        return 0;
    }

    @Override
    public void drawCardsAndPutSomeBackOnTop(int cardsToDraw, int cardsToPutBack) {
        List<Card> cards = drawCards(cardsToDraw);

        if (!cards.isEmpty()) {
            int cardsPutBack = 0;

            List<Card> sortedCards = cards.stream().sorted(returnCardToTopOfDeckScoreDescending).collect(toList());

            for (Card card : sortedCards) {
                if (cardsPutBack <= cardsToPutBack) {
                    getHand().remove(card);
                    addCardToTopOfDeck(card, false);
                    cardsToPutBack++;
                }
            }
        }
    }

    public void handleDeathWorld() {
        if (!getDiscard().isEmpty()) {
            List<Card> sortedDiscard = getDiscard()
                    .stream()
                    .filter(c -> c.hasFaction(Faction.TRADE_FEDERATION) || c.hasFaction(Faction.STAR_EMPIRE) || c.hasFaction(Faction.MACHINE_CULT))
                    .sorted(cardToBuyScoreAscending)
                    .collect(toList());

            if (!sortedDiscard.isEmpty()) {
                Card card = sortedDiscard.get(0);
                if (getBuyCardScore(card) <= 20) {
                    scrapCardFromDiscard(card);
                    drawCard();
                    return;
                }
            }
        }

        if (!getHand().isEmpty()) {
            List<Card> sortedHand = getHand()
                    .stream()
                    .filter(c -> c.hasFaction(Faction.TRADE_FEDERATION) || c.hasFaction(Faction.STAR_EMPIRE) || c.hasFaction(Faction.MACHINE_CULT))
                    .sorted(cardToBuyScoreAscending)
                    .collect(toList());

            if (!sortedHand.isEmpty()) {
                Card card = sortedHand.get(0);
                if (getBuyCardScore(card) <= 10) {
                    scrapCardFromHand(card);
                    drawCard();
                }
            }
        }
    }

    public void refreshGamePageForOpponent() {
        if (!getGame().isSimulation()) {
            sendGameMessageToOpponent("refresh_game_page");
        }
    }

    public void sendGameMessageToOpponent(String message) {
        sendGameMessage(getOpponent().getPlayerName(), message);
    }

    public void sendGameMessage(String recipient, String message) {
        if (!getGame().isSimulation()) {
            gameService.sendGameMessage(getPlayerName(), recipient, getGame().getGameId(), message);
        }
    }
}
