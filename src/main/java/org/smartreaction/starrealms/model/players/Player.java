package org.smartreaction.starrealms.model.players;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.TurnSummary;
import org.smartreaction.starrealms.model.cards.AlliableCard;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.cards.actions.*;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.blob.PlasmaVent;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.cards.bases.outposts.machinecult.StealthTower;
import org.smartreaction.starrealms.model.cards.bases.outposts.machinecult.WarningBeacon;
import org.smartreaction.starrealms.model.cards.events.Event;
import org.smartreaction.starrealms.model.cards.gambits.EveryTurnGambit;
import org.smartreaction.starrealms.model.cards.gambits.Gambit;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.cards.ships.DoNotBuyCard;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.cards.ships.Scout;
import org.smartreaction.starrealms.model.cards.ships.machinecult.StealthNeedle;
import org.smartreaction.starrealms.model.cards.ships.starempire.EmperorsDreadnaught;
import org.smartreaction.starrealms.model.cards.ships.tradefederation.ColonySeedShip;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class Player {
    private int authority = 50;
    private List<Card> deck = new ArrayList<>();
    private List<Card> hand = new ArrayList<>();
    private List<Card> discard = new ArrayList<>();
    private List<Card> played = new ArrayList<>();
    private List<Card> inPlay = new ArrayList<>();
    private List<Base> bases = new ArrayList<>();
    private List<Gambit> gambits = new ArrayList<>();
    private List<Hero> heroes = new ArrayList<>();
    private List<Mission> missions = new ArrayList<>();

    private List<Card> cardsInHandBeforeShuffle = new ArrayList<>();

    protected List<Action> actionsQueue = new ArrayList<>();

    protected Action currentAction;

    protected boolean yourTurn;

    private int combat;
    private int trade;

    private Game game;
    private Player opponent;

    private boolean nextShipToTopOfDeck;

    private boolean nextShipOrBaseToTopOfDeck;

    private boolean nextShipOrBaseToHand;

    private boolean nextBaseToHand;

    private boolean allShipsAddOneCombat;

    private boolean allFactionsAllied;

    private boolean preventFirstDamage;

    protected int shuffles;

    private boolean firstPlayer;

    protected String playerName;

    protected int turns;

    protected int turn;

    private boolean blobAlliedUntilEndOfTurn;
    private boolean starEmpireAlliedUntilEndOfTurn;
    private boolean tradeFederationAlliedUntilEndOfTurn;
    private boolean machineCultAlliedUntilEndOfTurn;

    private boolean gainTwoCombatWhenStarEmpireShipPlayed;

    private int numCardsScrappedThisTurn;
    private int tradeGainedThisTurn;
    private int combatGainedThisTurn;
    private int authorityGainedThisTurn;
    private List<Card> shipsPlayedThisTurn = new ArrayList<>();

    private Set<Faction> factionsPlayedThisTurn = new HashSet<>();
    private Set<Faction> factionsWithAllyAbilitiesUsedThisTurn = new HashSet<>();

    protected Comparator<Base> baseShieldAscending = (b1, b2) -> Integer.compare(b1.getShield(), b2.getShield());
    protected Comparator<Base> baseShieldDescending = baseShieldAscending.reversed();

    //these are for simulating which card is the best to buy
    protected Card cardToBuyThisTurn;
    protected Card cardToNotScrapThisTurn;
    protected Hero heroToNotPlayThisTurn;
    protected boolean firstTurn = true;
    protected boolean boughtSpecifiedCardOnFirstTurn = false;
    private long scoutsInFirstHand;
    private long scoutsInSecondHand;
    private Map<Integer, Set<Card>> cardsAcquiredByDeck = new HashMap<>();

    private TurnSummary lastTurnSummary;

    private TurnSummary currentTurnSummary = new TurnSummary();

    private String simulationPlayerId;

    private boolean waitingForComputer;

    private boolean acquireCardToTopOfDeck;

    private boolean acquireCardToHand;

    private boolean missionClaimedThisTurn;

    protected Player() {
    }

    @SuppressWarnings("unchecked")
    public void copyFromPlayerForSimulation(Player player, boolean opponent) {
        setSimulationPlayerId(player.getSimulationPlayerId());

        setAuthority(player.getAuthority());
        setCombat(player.getCombat());
        setTrade(player.getTrade());

        getDiscard().addAll(copyCards(player.getDiscard()));

        int handSize = player.getHand().size();

        if (opponent) {
            List<Card> handAndDeckCopy = new ArrayList<>(player.getHandAndDeck());

            handAndDeckCopy.removeAll(player.getCardsInHandBeforeShuffle());

            List<? extends Card> cardsInHandBeforeShuffleCopy = copyCards(player.getCardsInHandBeforeShuffle());

            List<? extends Card> deckCopy = copyCards(handAndDeckCopy);

            getDeck().addAll(deckCopy);
            Collections.shuffle(getDeck());

            getHand().addAll(cardsInHandBeforeShuffleCopy);

            drawCards(handSize - cardsInHandBeforeShuffleCopy.size());

            if (getGame().usingMissions()) {
                List<Mission> missionsToCopy = getClaimedMissions();

                if (missionsToCopy.size() < 3) {
                    List<Mission> availableMissions = new ArrayList<>(getGame().getAllMissions());
                    availableMissions.removeAll(player.getOpponent().getMissions());
                    availableMissions.removeAll(missionsToCopy);

                    Collections.shuffle(availableMissions);

                    missionsToCopy.addAll(availableMissions.subList(0, 3 - missionsToCopy.size()));
                }

                getMissions().addAll(copyMissions(missionsToCopy));
            }
        } else {
            getHand().addAll(copyCards(player.getHand()));
            getDeck().addAll(copyCards(player.getDeck()));
            Collections.shuffle(getDeck());
            if (getGame().usingMissions()) {
                getMissions().addAll(copyMissions(player.getMissions()));
            }
        }

        getInPlay().addAll(copyCards(player.getInPlay()));

        List<Base> copyOfBases = new ArrayList<>();
        for (Card card : getInPlay()) {
            if (card instanceof Base) {
                copyOfBases.add((Base) card);
            }
        }
        getBases().addAll(copyOfBases);

        getPlayed().addAll(copyCards(player.getPlayed()));
        getHeroes().addAll((Collection<? extends Hero>) copyCards(player.getHeroes()));
        getGambits().addAll((Collection<? extends Gambit>) copyCards(player.getGambits()));

        shuffles = player.getShuffles();
        turn = player.getTurn();
        turns = player.getTurns();

        firstPlayer = player.isFirstPlayer();

        yourTurn = player.isYourTurn();

        blobAlliedUntilEndOfTurn = player.isBlobAlliedUntilEndOfTurn();
        starEmpireAlliedUntilEndOfTurn = player.isStarEmpireAlliedUntilEndOfTurn();
        tradeFederationAlliedUntilEndOfTurn = player.isTradeFederationAlliedUntilEndOfTurn();
        machineCultAlliedUntilEndOfTurn = player.isMachineCultAlliedUntilEndOfTurn();

        nextBaseToHand = player.isNextBaseToHand();
        nextShipOrBaseToTopOfDeck = player.isNextShipOrBaseToTopOfDeck();
        nextShipOrBaseToHand = player.isNextShipOrBaseToHand();
        nextShipToTopOfDeck = player.isNextShipToTopOfDeck();

        allShipsAddOneCombat = player.isAllShipsAddOneCombat();
        allFactionsAllied = player.isAllFactionsAllied();
        gainTwoCombatWhenStarEmpireShipPlayed = player.isGainTwoCombatWhenStarEmpireShipPlayed();

        cardToBuyThisTurn = player.getCardToBuyThisTurn();
        cardToNotScrapThisTurn = player.getCardToNotScrapThisTurn();
        heroToNotPlayThisTurn = player.getHeroToNotPlayThisTurn();

        numCardsScrappedThisTurn = player.getNumCardsScrappedThisTurn();
        tradeGainedThisTurn = player.getTradeGainedThisTurn();
        combatGainedThisTurn = player.getCombatGainedThisTurn();
        authorityGainedThisTurn = player.getAuthorityGainedThisTurn();
        getShipsPlayedThisTurn().addAll(copyCards(player.getShipsPlayedThisTurn()));

        factionsPlayedThisTurn = new HashSet<>(player.getFactionsPlayedThisTurn());
        factionsWithAllyAbilitiesUsedThisTurn = new HashSet<>(player.getFactionsWithAllyAbilitiesUsedThisTurn());
    }

    private List<? extends Card> copyCards(List<? extends Card> cardsToCopy) {
        return cardsToCopy
                .stream()
                .map(Card::copyCardForSimulation)
                .collect(toList());
    }

    private List<Mission> copyMissions(List<Mission> missionsToCopy) {
        return missionsToCopy
                .stream()
                .map(Mission::copyMissionForSimulation)
                .collect(toList());
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public int getAuthority() {
        return authority;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public int getCombat() {
        return combat;
    }

    public void setCombat(int combat) {
        this.combat = combat;
    }

    public int getTrade() {
        return trade;
    }

    public void setTrade(int trade) {
        this.trade = trade;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Card> getCardsInHandBeforeShuffle() {
        return cardsInHandBeforeShuffle;
    }

    public List<Card> getDiscard() {
        return discard;
    }

    public List<Card> getPlayed() {
        return played;
    }

    public List<Card> getInPlay() {
        return inPlay;
    }

    public List<Base> getBases() {
        return bases;
    }

    public List<Outpost> getOutposts() {
        return bases.stream().filter(Card::isOutpost).map(base -> (Outpost) base).collect(Collectors.toList());
    }

    public int getShuffles() {
        return shuffles;
    }

    public void drawCard() {
        drawCards(1);
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public List<Card> drawCards(int cards) {
        if (cards == 0) {
            return new ArrayList<>();
        }

        List<Card> cardsDrawn = new ArrayList<>();
        String log = playerName + " drawing " + cards;
        if (cards == 1) {
            log += " card";
        } else {
            log += " cards";
        }
        addGameLog(log);

        for (int i = 0; i < cards; i++) {
            if (deck.isEmpty()) {
                cardsInHandBeforeShuffle.addAll(cardsDrawn);
                shuffleDiscardIntoDeck();
            }

            if (!deck.isEmpty()) {
                Card cardToDraw = deck.remove(0);
                cardsDrawn.add(cardToDraw);
                addCardToHand(cardToDraw, false);
            }
        }

        if (yourTurn && deck.isEmpty()) {
            cardsInHandBeforeShuffle.addAll(cardsDrawn);
        }

        return cardsDrawn;
    }

    private void shuffleDiscardIntoDeck() {
        deck.addAll(discard);
        discard.clear();
        addGameLog("Shuffling deck");
        Collections.shuffle(deck);
        shuffles++;
    }

    public void opponentDiscardsCard() {
        addGameLog("Opponent discarding card");
        opponent.discardCardFromHand();
    }

    private void cardRemovedFromPlay(Card card) {
        card.setAllAlliedAbilitiesToNotUsed();

        if (card instanceof Base) {
            ((Base) card).setUsed(false);
        }

        card.removedFromPlay(this);
    }

    public void addTrade(int trade) {
        this.trade += trade;
        tradeGainedThisTurn += trade;
    }

    public void addCombat(int combat) {
        this.combat += combat;
        combatGainedThisTurn += combat;
    }

    public void addAuthority(int authority) {
        this.authority += authority;
        authorityGainedThisTurn += authority;
        if (yourTurn) {
            currentTurnSummary.setAuthorityGained(currentTurnSummary.getAuthorityGained() + authority);
        }
    }

    public void endTurn() {
        addGameLog("Ending turn");

        currentTurnSummary.getCardsPlayed().addAll(played);

        lastTurnSummary = currentTurnSummary;

        turns++;

        firstTurn = false;

        missionClaimedThisTurn = false;

        if (cardToBuyThisTurn != null && cardToBuyThisTurn instanceof DoNotBuyCard) {
            boughtSpecifiedCardOnFirstTurn = true;
        }

        cardToBuyThisTurn = null;
        cardToNotScrapThisTurn = null;

        combat = 0;
        trade = 0;

        nextShipToTopOfDeck = false;
        nextShipOrBaseToTopOfDeck = false;
        nextShipOrBaseToHand = false;
        nextBaseToHand = false;

        blobAlliedUntilEndOfTurn = false;
        starEmpireAlliedUntilEndOfTurn = false;
        tradeFederationAlliedUntilEndOfTurn = false;
        machineCultAlliedUntilEndOfTurn = false;

        numCardsScrappedThisTurn = 0;
        tradeGainedThisTurn = 0;
        combatGainedThisTurn = 0;
        authorityGainedThisTurn = 0;
        shipsPlayedThisTurn.clear();

        factionsPlayedThisTurn.clear();
        factionsWithAllyAbilitiesUsedThisTurn.clear();

        played.clear();

        for (Card card : inPlay) {
            if (card.isBase()) {
                ((Base) card).onEndTurn();
                ((Base) card).setUsed(false);
                card.setAllAlliedAbilitiesToNotUsed();
            } else {
                discard.add(card);
                cardRemovedFromPlay(card);
            }
        }

        inPlay.clear();

        discard.addAll(hand);
        hand.clear();
        cardsInHandBeforeShuffle.clear();

        drawCards(5);

        yourTurn = false;

        game.turnEnded();
    }

    public void addBase(Base base) {
        this.getBases().add(base);
    }

    public abstract void destroyTargetBase();

    public void baseDestroyed(Base base) {
        addGameLog("Destroyed base: " + base.getName());
        bases.remove(base);
        discard.add(base);
        cardRemovedFromPlay(base);
        if (!yourTurn) {
            getOpponent().getCurrentTurnSummary().getOpponentBasesDestroyed().add(base);
        }
    }

    public List<Base> getUnusedBasesAndOutposts() {
        List<Base> unusedBases = new ArrayList<>();

        for (Base base : bases) {
            if (!base.isUsed()) {
                unusedBases.add(base);
            }
        }

        return unusedBases;
    }

    public int getSmallestBaseShield() {
        if (!getBases().isEmpty()) {
            List<Base> sortedBases = getBases().stream().sorted(baseShieldAscending).collect(toList());
            return sortedBases.get(0).getShield();
        }
        return 0;
    }

    public int getSmallestOutpostShield() {
        if (!getOutposts().isEmpty()) {
            List<Outpost> sortedOutposts = getOutposts().stream().sorted(baseShieldAscending).collect(toList());
            return sortedOutposts.get(0).getShield();
        }
        return 0;
    }

    public int getBiggestBaseShield() {
        if (!getBases().isEmpty()) {
            List<Base> sortedBases = getBases().stream().sorted(baseShieldDescending).collect(toList());
            return sortedBases.get(0).getShield();
        }
        return 0;
    }

    public int getBiggestOutpostShield() {
        if (!getOutposts().isEmpty()) {
            List<Outpost> sortedOutposts = getOutposts().stream().sorted(baseShieldDescending).collect(toList());
            return sortedOutposts.get(0).getShield();
        }
        return 0;
    }

    public void optionallyScrapCardFromHandOrDiscard() {
        optionallyScrapCardsFromHandOrDiscard(1);
    }

    public abstract void optionallyScrapCardsFromHandOrDiscard(int cards);

    public void optionallyScrapCardsFromHandOrDiscardForBenefit(ScrapCardsForBenefitActionCard card, int numCardsToScrap) {
        optionallyScrapCardsFromHandOrDiscardForBenefit(card, numCardsToScrap, "Scrap up to " + numCardsToScrap + " from your hand or discard pile");
    }

    public abstract void optionallyScrapCardsFromHandOrDiscardForBenefit(ScrapCardsForBenefitActionCard card, int numCardsToScrap, String text);

    public abstract void optionallyScrapCardsFromHandOrDiscardOrTradeRow(int cards);

    public abstract void optionallyDiscardCardsForBenefit(DiscardCardsForBenefitActionCard card, int numCardsToDiscard, String text);

    public abstract void discardCardsForBenefit(DiscardCardsForBenefitActionCard card, int numCardsToDiscard, String text);

    public void scrapCardFromDiscard(Card card) {
        addGameLog("Scrapped " + card.getName() + " from discard");
        discard.remove(card);
        playerCardScrapped(card);
    }

    public void scrapCardFromHand(Card card) {
        addGameLog("Scrapped " + card.getName() + " from hand");
        hand.remove(card);
        cardsInHandBeforeShuffle.remove(card);
        playerCardScrapped(card);
    }

    private void playerCardScrapped(Card card) {
        if (!card.isStarterCard() && !(card instanceof Explorer)) {
            game.getTradeRowCardsScrapped().add(card);
        }
        cardRemovedFromPlay(card);
        numCardsScrappedThisTurn++;

        if (yourTurn) {
            currentTurnSummary.getCardsScrapped().add(card);
        }
    }

    public void scrapCardInPlayForBenefit(Card card) {
        if (card.isScrappable()) {
            addGameLog("Scrapped " + card.getName() + " from in play for benefit");
            inPlay.remove(card);
            if (card instanceof Base) {
                bases.remove(card);
            } else if (card instanceof Hero) {
                heroes.remove(card);
            } else if (card instanceof Gambit) {
                gambits.remove(card);
            }
            playerCardScrapped(card);

            if (card instanceof StealthNeedle) {
                StealthNeedle stealthNeedle = (StealthNeedle) card;
                if (stealthNeedle.getCardBeingCopied() != null) {
                    ((ScrappableCard) stealthNeedle.getCardBeingCopied()).cardScrapped(this);
                }
            } else if (card instanceof StealthTower) {
                StealthTower stealthTower = (StealthTower) card;
                if (stealthTower.getCardBeingCopied() != null) {
                    ((ScrappableCard) stealthTower.getCardBeingCopied()).cardScrapped(this);
                }
            } else {
                ((ScrappableCard) card).cardScrapped(this);
            }

            for (Card c : inPlay) {
                allyCardIfAvailable(c);
            }
        }
    }

    public void optionalScrapCardInTradeRow() {
        optionallyScrapCardsInTradeRow(1);
    }

    public abstract void optionallyScrapCardsInTradeRow(int cards);

    public void acquireCardToTopOfDeck(Card card) {
        acquireCardToTopOfDeck = true;
        cardAcquired(card);
    }

    public void acquireCardToHand(Card card) {
        acquireCardToHand = true;
        cardAcquired(card);
    }

    public void addCardToTopOfDeck(Card card) {
        addCardToTopOfDeck(card, true);
    }

    public void addCardToTopOfDeck(Card card, boolean addGameLog) {
        if (card instanceof Hero) {
            heroes.add((Hero) card);
            if (addGameLog) {
                addGameLog("Added " + card.getName() + " to heroes");
            }
        } else {
            deck.add(0, card);
            if (addGameLog) {
                addGameLog("Added " + card.getName() + " to top of deck");
            }
        }
    }

    public void addCardToHand(Card card) {
        addCardToHand(card, true);
    }

    public void addCardToHand(Card card, boolean addToGameLog) {
        hand.add(card);
        if (addToGameLog) {
            addGameLog("Added " + card.getName() + " to hand");
        }
    }

    public void cardAcquired(Card card) {
        Set<Card> cardsAcquiredInCurrentDeck = cardsAcquiredByDeck.get(getCurrentDeckNumber());
        if (cardsAcquiredInCurrentDeck == null) {
            cardsAcquiredInCurrentDeck = new HashSet<>();
        }
        cardsAcquiredInCurrentDeck.add(card);
        cardsAcquiredByDeck.put(getCurrentDeckNumber(), cardsAcquiredInCurrentDeck);

        if (acquireCardToHand) {
            acquireCardToHand = false;
            addCardToHand(card);
        } else if (acquireCardToTopOfDeck) {
            acquireCardToTopOfDeck = false;
            addCardToTopOfDeck(card);
        } else if ((card instanceof ColonySeedShip && factionPlayedThisTurn(Faction.TRADE_FEDERATION)) ||
                (card instanceof EmperorsDreadnaught && factionPlayedThisTurn(Faction.STAR_EMPIRE)) ||
                (card instanceof PlasmaVent && blobCardPlayedThisTurn()) ||
                (card instanceof WarningBeacon && machineCultCardPlayedThisTurn())) {
            addCardToHand(card);
        } else if (card instanceof Hero) {
            Hero hero = (Hero) card;
            heroes.add(hero);
            hero.heroAcquired(this);
        } else if (card.isShip() && (nextShipToTopOfDeck || nextShipOrBaseToTopOfDeck)) {
            nextShipToTopOfDeck = false;
            nextShipOrBaseToTopOfDeck = false;
            addCardToTopOfDeck(card);
        } else if (card.isShip() && nextShipOrBaseToHand) {
            nextShipOrBaseToHand = false;
            addCardToHand(card);
        } else if (card.isBase() && (nextBaseToHand || nextShipOrBaseToHand)) {
            nextBaseToHand = false;
            nextShipOrBaseToHand = false;
            addCardToHand(card);
        } else if (card.isBase() && nextShipOrBaseToTopOfDeck) {
            nextShipOrBaseToTopOfDeck = false;
            addCardToTopOfDeck(card);
        } else {
            discard.add(card);
        }

        if (cardToBuyThisTurn != null && cardToBuyThisTurn.getName().equals(card.getName())) {
            cardToBuyThisTurn = null;
            if (firstTurn) {
                boughtSpecifiedCardOnFirstTurn = true;
            }
        }

        if (yourTurn) {
            currentTurnSummary.getCardsAcquired().add(card);
        }
    }

    public abstract void makeChoice(ChoiceActionCard card, Choice... choices);

    public abstract void makeChoice(ChoiceActionCard card, String text, Choice... choices);

    public void nextShipToTopOfDeck() {
        nextShipToTopOfDeck = true;
    }

    public void nextShipOrBaseToTopOfDeck() {
        nextShipOrBaseToTopOfDeck = true;
    }

    public void nextShipOrBaseToHand() {
        nextShipOrBaseToHand = true;
    }

    public void nextBaseToHand() {
        nextBaseToHand = true;
    }

    public void setAllShipsAddOneCombat(boolean allShipsAddOneCombat) {
        this.allShipsAddOneCombat = allShipsAddOneCombat;
    }

    public abstract void scrapCardFromHand(boolean optional);

    public abstract void optionallyScrapCardsFromDiscard(int cards);

    public void setAllFactionsAllied(boolean allFactionsAllied) {
        this.allFactionsAllied = allFactionsAllied;
    }

    public void buyCard(Card card) {
        if (trade >= card.getCost()) {
            addGameLog("Bought card: " + card.getName());
            trade -= card.getCost();
            if (card instanceof Explorer) {
                cardAcquired(card);
            } else {
                game.getTradeRow().remove(card);
                game.addCardToTradeRow();
                cardAcquired(card);
            }
        }
    }

    public void destroyOpponentBase(Base base) {
        combat -= base.getShield();

        opponent.baseDestroyed(base);
    }

    public void reduceAuthority(int amount) {
        authority -= amount;
    }

    public void attackOpponentWithRemainingCombat() {
        addGameLog("Applied " + combat + " combat to opponent");
        if (opponent.isPreventFirstDamage() && combat > 0) {
            combat--;
            opponent.setPreventFirstDamage(false);
        }

        opponent.reduceAuthority(combat);

        currentTurnSummary.setDamageToOpponent(currentTurnSummary.getDamageToOpponent() + combat);

        combat = 0;
    }

    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        cards.addAll(hand);
        cards.addAll(deck);
        cards.addAll(discard);
        if (inPlay.isEmpty()) {
            cards.addAll(bases);
        } else {
            cards.addAll(inPlay);
        }
        cards.addAll(heroes);

        return cards;
    }

    public boolean useAlliedAbilities(Card card) {
        Card cardToUse = card;

        if (card instanceof StealthNeedle && ((StealthNeedle) card).getCardBeingCopied() != null) {
            cardToUse = ((StealthNeedle) card).getCardBeingCopied();
        }
        if (card instanceof StealthTower && ((StealthTower) card).getCardBeingCopied() != null) {
            cardToUse = ((StealthTower) card).getCardBeingCopied();
        }

        boolean allyAbilityUsed = false;

        if (cardHasAnyUnusedAlly(cardToUse)) {
            for (Faction faction : cardToUse.getFactions()) {
                if (!cardToUse.isAlliedAbilityUsed(faction) && cardHasAlly(cardToUse, faction)) {
                    cardToUse.setAlliedAbilityUsed(true, faction);
                    factionsWithAllyAbilitiesUsedThisTurn.add(faction);
                    ((AlliableCard) cardToUse).cardAllied(this, faction);
                    allyAbilityUsed = true;
                }
            }
        }

        return allyAbilityUsed;
    }

    public boolean cardHasAnyUnusedAlly(Card card) {
        for (Faction faction : card.getFactions()) {
            if (!card.isAlliedAbilityUsed(faction) && cardHasAlly(card, faction)) {
                return true;
            }
        }

        return false;
    }

    public boolean cardHasAlly(Card card, Faction faction) {
        if (allFactionsAllied) {
            game.gameLog("All factions allied");
            return true;
        }

        if (card.hasFaction(Faction.BLOB) && blobAlliedUntilEndOfTurn) {
            return true;
        }

        if (card.hasFaction(Faction.STAR_EMPIRE) && starEmpireAlliedUntilEndOfTurn) {
            return true;
        }

        if (card.hasFaction(Faction.TRADE_FEDERATION) && tradeFederationAlliedUntilEndOfTurn) {
            return true;
        }

        if (card.hasFaction(Faction.MACHINE_CULT) && machineCultAlliedUntilEndOfTurn) {
            return true;
        }

        for (Card c : inPlay) {
            if (!c.equals(card) && c.getAlliedFactions(card).contains(faction)) {
                return true;
            }
        }

        return false;
    }

    public void playCard(Card card) {
        if (!card.isCopied()) {
            game.gameLog("Played card: " + card.getName());

            played.add(card);
            inPlay.add(card);
            hand.remove(card);
            cardsInHandBeforeShuffle.remove(card);

            if (card.isBase()) {
                addBase((Base) card);
            }
        }

        if (card.isShip()) {
            shipsPlayedThisTurn.add(card);
            if (allShipsAddOneCombat) {
                addCombat(1);
            }
            if (card.hasFaction(Faction.STAR_EMPIRE) && gainTwoCombatWhenStarEmpireShipPlayed) {
                addCombat(2);
            }
        }

        factionsPlayedThisTurn.addAll(card.getFactions());

        card.cardPlayed(this);

        for (Card c : inPlay) {
            allyCardIfAvailable(c);
        }
    }

    private void allyCardIfAvailable(Card card) {
        if (card.isAlliableCard()) {
            if (card.hasUnusedAllyAbility() && card.isAutoAlly()) {
                for (Faction faction : card.getFactions()) {
                    if (!card.getAutoAllyExcludedFactions().contains(faction)
                            && !card.isAlliedAbilityUsed(faction)
                            && cardHasAlly(card, faction)) {
                        card.setAlliedAbilityUsed(true, faction);
                        factionsWithAllyAbilitiesUsedThisTurn.add(faction);
                        card.getAlliableCard().cardAllied(this, faction);
                    }
                }
            }
        }
    }

    public int countCardsByType(List<Card> cards, Function<Card, Boolean> typeMatcher) {
        return (int) cards.stream().filter(typeMatcher::apply).count();
    }

    public boolean basePlayedThisTurn() {
        for (Card card : getPlayed()) {
            if (card.isBase()) {
                return true;
            }
        }
        return false;
    }

    public abstract void returnTargetBaseToHand();

    public int getCurrentDeckNumber() {
        return getShuffles() + 1;
    }

    public boolean isPreventFirstDamage() {
        return preventFirstDamage;
    }

    public void setPreventFirstDamage(boolean preventFirstDamage) {
        this.preventFirstDamage = preventFirstDamage;
    }

    public abstract void acquireFreeCard(int maxCost);

    public abstract void acquireFreeCardToTopOfDeck(int maxCost);

    public void acquireFreeShipToTopOfDeck() {
        acquireFreeShipToTopOfDeck(null);
    }

    public abstract void acquireFreeShipToTopOfDeck(Integer maxCost);

    public abstract void acquireFreeCardToHand(int maxCost, boolean includeHeroes);

    public List<Gambit> getGambits() {
        return gambits;
    }

    public List<Gambit> getScrappableGambits() {
        return gambits.stream().filter(g -> g instanceof ScrappableCard).collect(toList());
    }

    public List<Gambit> getEveryTurnGambits() {
        return gambits.stream().filter(g -> g instanceof EveryTurnGambit).collect(toList());
    }

    public void setGambits(List<Gambit> gambits) {
        this.gambits = gambits;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getTurn() {
        return turn;
    }

    public int getTurns() {
        return turns;
    }

    public Faction getFactionWithMostCards() {
        List<Card> cards = getAllCards();

        Map<Faction, Integer> factionCounts = new HashMap<>(4);

        factionCounts.put(Faction.BLOB, countCardsByType(cards, c -> c.hasFaction(Faction.BLOB)));
        factionCounts.put(Faction.STAR_EMPIRE, countCardsByType(cards, c -> c.hasFaction(Faction.STAR_EMPIRE)));
        factionCounts.put(Faction.TRADE_FEDERATION, countCardsByType(cards, c -> c.hasFaction(Faction.TRADE_FEDERATION)));
        factionCounts.put(Faction.MACHINE_CULT, countCardsByType(cards, c -> c.hasFaction(Faction.MACHINE_CULT)));

        Faction factionWithMostCards = null;
        int highestFactionCount = 0;

        for (Faction faction : factionCounts.keySet()) {
            if (factionCounts.get(faction) >= highestFactionCount) {
                factionWithMostCards = faction;
            }
        }

        return factionWithMostCards;
    }

    public abstract void drawCardsAndPutSomeBackOnTop(int cardsToDraw, int cardsToPutBack);

    public void blobAlliedUntilEndOfTurn() {
        blobAlliedUntilEndOfTurn = true;
    }

    public void starEmpireAlliedUntilEndOfTurn() {
        starEmpireAlliedUntilEndOfTurn = true;
    }

    public void tradeFederationAlliedUntilEndOfTurn() {
        tradeFederationAlliedUntilEndOfTurn = true;
    }

    public void machineCultAlliedUntilEndOfTurn() {
        machineCultAlliedUntilEndOfTurn = true;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public boolean factionPlayedThisTurn(Faction faction) {
        return factionsPlayedThisTurn.contains(faction);
    }

    public void setGainTwoCombatWhenStarEmpireShipPlayed(boolean gainTwoCombatWhenStarEmpireShipPlayed) {
        this.gainTwoCombatWhenStarEmpireShipPlayed = gainTwoCombatWhenStarEmpireShipPlayed;
    }

    public boolean blobCardPlayedThisTurn() {
        return cardPlayedThisTurn(c -> c.hasFaction(Faction.BLOB));
    }

    public boolean tradeFederationCardPlayedThisTurn() {
        return cardPlayedThisTurn(c -> c.hasFaction(Faction.TRADE_FEDERATION));
    }

    public boolean starEmpireCardPlayedThisTurn() {
        return cardPlayedThisTurn(c -> c.hasFaction(Faction.STAR_EMPIRE));
    }

    public boolean machineCultCardPlayedThisTurn() {
        return cardPlayedThisTurn(c -> c.hasFaction(Faction.MACHINE_CULT));
    }

    public boolean cardPlayedThisTurn(Function<Card, Boolean> typeMatcher) {
        return countCardsByType(played, typeMatcher) > 0;
    }

    public boolean canOnlyDestroyBaseWithExtraCombat(int extraCombat) {
        if (getOpponent().getBases().size() > 0) {
            if (getOpponent().getOutposts().size() > 0) {
                if (getCombat() < getOpponent().getSmallestOutpostShield()) {
                    return (getCombat() + extraCombat) >= getOpponent().getSmallestOutpostShield();
                }
            } else if (getOpponent().getBases().size() > 0) {
                if (getCombat() < getOpponent().getSmallestBaseShield()) {
                    return (getCombat() + extraCombat) >= getOpponent().getSmallestBaseShield();
                }
            }
        }
        return false;
    }

    public int getNumBlobCardsPlayedThisTurn() {
        return countCardsByType(played, c -> c.hasFaction(Faction.BLOB));
    }

    public List getHandAndDeck() {
        List<Card> cards = new ArrayList<>(hand);
        cards.addAll(deck);
        return cards;
    }

    public void addGameLog(String log) {
        if (getGame() != null) {
            getGame().gameLog(log);
        }
    }

    public void addCardToDiscard(Card card) {
        discard.add(card);
    }

    public void discardCardFromHand() {
        discardCardsFromHand(1);
    }

    public void discardCardFromHand(Card card) {
        hand.remove(card);
        cardsInHandBeforeShuffle.remove(card);
        addCardToDiscard(card);
        addGameLog(playerName + " discarded " + card.getName() + " from hand");
    }

    public abstract void discardCardsFromHand(int cards);

    public void resolveActions() {
        if (!actionsQueue.isEmpty()) {
            Action action = actionsQueue.remove(0);
            processNextAction(action);
        }
    }

    private void processNextAction(Action action) {
        if (action.processAction(this)) {
            currentAction = action;
        } else {
            action.onNotUsed(this);
            resolveActions();
        }
    }

    public Action getCurrentAction() {
        return currentAction;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public boolean isCardBuyable(Card card) {
        //noinspection SimplifiableIfStatement
        return yourTurn && card.getCost() <= trade;
    }

    public void addCardToDeck(Card card) {
        deck.add(card);
    }

    public void setup() {
        Collections.shuffle(deck);
        if (isFirstPlayer()) {
            drawCards(3);
        } else {
            drawCards(5);
        }
    }

    public void actionResult(Action action, ActionResult result) {
        if (result.getSelectedCard() != null || result.getChoiceSelected() != null
                || result.isDoNotUse() || result.isDoneWithAction()) {
            if (result.isDoNotUse() || action.processActionResult(this, result)) {
                if (result.isDoNotUse()) {
                    currentAction.onNotUsed(this);
                }
                currentAction = null;
                resolveActions();
            }
        }
    }

    public void playAll() {
        List<Card> copyOfHand = new ArrayList<>(hand);
        copyOfHand.forEach(this::playCard);
    }

    public void startTurn() {
        waitingForComputer = false;
        yourTurn = true;
        turn++;
        addGameLog("");
        addGameLog("*** " + playerName + "'s Turn " + turn + " ***");
        addGameLog("Deck: " + getCurrentDeckNumber());

        currentTurnSummary = new TurnSummary();
        currentTurnSummary.setGameTurn(getGame().getTurn());

        inPlay.addAll(bases);

        long scoutsInHand = hand.stream().filter(c -> c instanceof Scout).count();
        if (turns == 0) {
            scoutsInFirstHand = scoutsInHand;
        } else if (turns == 1) {
            scoutsInSecondHand = scoutsInHand;
        }

        for (Base base : bases) {
            if (base.isAutoUse()) {
                base.useBase(this);
            }

            allyCardIfAvailable(base);
        }

        if (!gambits.isEmpty()) {
            List<Gambit> everyTurnGambits = getEveryTurnGambits();
            for (Gambit gambit : everyTurnGambits) {
                ((EveryTurnGambit) gambit).everyTurnAbility(this);
            }
        }

        resolveActions();

        takeTurn();
    }

    public abstract void takeTurn();

    public int getNumCardsScrappedThisTurn() {
        return numCardsScrappedThisTurn;
    }

    public void loseAuthorityFromEvent(int authorityLost) {
        if (authority <= authorityLost) {
            authority = 1;
        } else {
            reduceAuthority(authorityLost);
        }
    }

    public int getNumBasesInAllCards() {
        return (int) getAllCards().stream().filter(Card::isBase).count();
    }

    public abstract void destroyOwnBase(DestroyOwnBaseActionCard card, String text);

    public abstract void addCardAction(CardActionCard card, String text);

    public abstract void showTriggeredEvent(Event event);

    public abstract void addCardFromDiscardToTopOfDeck(Integer maxCost);

    public Card getCardToBuyThisTurn() {
        return cardToBuyThisTurn;
    }

    public void setCardToBuyThisTurn(Card cardToBuyThisTurn) {
        this.cardToBuyThisTurn = cardToBuyThisTurn;
    }

    public Card getCardToNotScrapThisTurn() {
        return cardToNotScrapThisTurn;
    }

    public void setCardToNotScrapThisTurn(Card cardToNotScrapThisTurn) {
        this.cardToNotScrapThisTurn = cardToNotScrapThisTurn;
    }

    public Hero getHeroToNotPlayThisTurn() {
        return heroToNotPlayThisTurn;
    }

    public void setHeroToNotPlayThisTurn(Hero heroToNotPlayThisTurn) {
        this.heroToNotPlayThisTurn = heroToNotPlayThisTurn;
    }

    public boolean isBoughtSpecifiedCardOnFirstTurn() {
        return boughtSpecifiedCardOnFirstTurn;
    }

    public long getScoutsInFirstHand() {
        return scoutsInFirstHand;
    }

    public long getScoutsInSecondHand() {
        return scoutsInSecondHand;
    }

    public Map<Integer, Set<Card>> getCardsAcquiredByDeck() {
        return cardsAcquiredByDeck;
    }

    public TurnSummary getLastTurnSummary() {
        return lastTurnSummary;
    }

    public TurnSummary getCurrentTurnSummary() {
        return currentTurnSummary;
    }

    public boolean isBot() {
        return this instanceof BotPlayer;
    }

    public boolean isBlobAlliedUntilEndOfTurn() {
        return blobAlliedUntilEndOfTurn;
    }

    public boolean isStarEmpireAlliedUntilEndOfTurn() {
        return starEmpireAlliedUntilEndOfTurn;
    }

    public boolean isTradeFederationAlliedUntilEndOfTurn() {
        return tradeFederationAlliedUntilEndOfTurn;
    }

    public boolean isMachineCultAlliedUntilEndOfTurn() {
        return machineCultAlliedUntilEndOfTurn;
    }

    public boolean isNextShipToTopOfDeck() {
        return nextShipToTopOfDeck;
    }

    public boolean isNextShipOrBaseToTopOfDeck() {
        return nextShipOrBaseToTopOfDeck;
    }

    public boolean isNextShipOrBaseToHand() {
        return nextShipOrBaseToHand;
    }

    public boolean isNextBaseToHand() {
        return nextBaseToHand;
    }

    public boolean isAllShipsAddOneCombat() {
        return allShipsAddOneCombat;
    }

    public boolean isAllFactionsAllied() {
        return allFactionsAllied;
    }

    public boolean isGainTwoCombatWhenStarEmpireShipPlayed() {
        return gainTwoCombatWhenStarEmpireShipPlayed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(simulationPlayerId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        return Objects.equals(this.simulationPlayerId, other.simulationPlayerId);
    }

    public String getSimulationPlayerId() {
        return simulationPlayerId;
    }

    public void setSimulationPlayerId(String simulationPlayerId) {
        this.simulationPlayerId = simulationPlayerId;
    }

    public boolean isWaitingForComputer() {
        return waitingForComputer;
    }

    public void setWaitingForComputer(boolean waitingForComputer) {
        this.waitingForComputer = waitingForComputer;
    }

    public int getTradeGainedThisTurn() {
        return tradeGainedThisTurn;
    }

    public int getCombatGainedThisTurn() {
        return combatGainedThisTurn;
    }

    public int getAuthorityGainedThisTurn() {
        return authorityGainedThisTurn;
    }

    public List<Card> getShipsPlayedThisTurn() {
        return shipsPlayedThisTurn;
    }

    public Set<Faction> getFactionsPlayedThisTurn() {
        return factionsPlayedThisTurn;
    }

    public Set<Faction> getFactionsWithAllyAbilitiesUsedThisTurn() {
        return factionsWithAllyAbilitiesUsedThisTurn;
    }

    public List<Card> revealTopCardsOfDeck(int cards) {
        List<Card> revealedCards = new ArrayList<>();

        if (deck.size() < cards) {
            //todo account for cards in hand before shuffle list
            shuffleDiscardIntoDeck();
        }

        if (deck.isEmpty()) {
            addGameLog(playerName + " had no cards to reveal");
        } else {
            for (int i = 0; i < cards; i++) {
                if (deck.size() < i+1) {
                    addGameLog("No more cards to reveal");
                } else {
                    Card card = deck.get(i);
                    addGameLog(playerName + " revealed " + card.getName() + " from top of deck");
                    revealedCards.add(card);
                }
            }
        }

        return revealedCards;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public void setMissions(List<Mission> missions) {
        this.missions = missions;
    }

    public List<Mission> getClaimedMissions() {
        return missions.stream().filter(Mission::isMissionClaimed).collect(toList());
    }

    public List<Mission> getUnClaimedMissions() {
        return missions.stream().filter(m -> !m.isMissionClaimed()).collect(toList());
    }

    public void claimMission(Mission mission) {
        addGameLog(getPlayerName() + " completed the " + mission.getName() + " mission");
        missionClaimedThisTurn = true;
        mission.setMissionClaimed(true);
        mission.onMissionClaimed(this);
    }

    public boolean isMissionClaimedThisTurn() {
        return missionClaimedThisTurn;
    }

    public int getTotalCombatIfAllCardsInPlayScrapped() {
        return getInPlayAndHeroes().stream()
                .filter(c -> c.getCombatWhenScrapped() > 0)
                .mapToInt(Card::getCombatWhenScrapped)
                .sum();
    }

    public List<Card> getInPlayAndHeroes() {
        List<Card> cards = new ArrayList<>(inPlay);
        cards.addAll(heroes);
        return cards;
    }
}
