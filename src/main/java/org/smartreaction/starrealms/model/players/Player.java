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

    private Set<Faction> factionsPlayedThisTurn = new HashSet<>();

    protected Comparator<Base> baseShieldAscending = (b1, b2) -> Integer.compare(b1.getShield(), b2.getShield());
    protected Comparator<Base> baseShieldDescending = baseShieldAscending.reversed();

    //these are for simulating which card is the best to buy
    protected Card cardToBuyThisTurn;
    protected boolean firstTurn = true;
    protected boolean boughtSpecifiedCardOnFirstTurn = false;
    private long scoutsInFirstHand;
    private long scoutsInSecondHand;
    private Map<Integer, Set<Card>> cardsAcquiredByDeck = new HashMap<>();

    private TurnSummary lastTurnSummary;

    private TurnSummary currentTurnSummary = new TurnSummary();

    protected Player() {
    }

    @SuppressWarnings("unchecked")
    public void copyFromPlayerForSimulation(Player player) {
        setAuthority(player.getAuthority());
        setCombat(player.getCombat());
        setTrade(player.getTrade());

        int handSize = player.getHand().size();

        List<Card> handAndDeckCopy = new ArrayList<>(player.getHandAndDeck());

        handAndDeckCopy.removeAll(player.getCardsInHandBeforeShuffle());

        List<? extends Card> cardsInHandBeforeShuffleCopy = copyCards(player.getCardsInHandBeforeShuffle());

        List<? extends Card> deckCopy = copyCards(handAndDeckCopy);

        getDeck().addAll(deckCopy);
        Collections.shuffle(getDeck());

        getHand().addAll(cardsInHandBeforeShuffleCopy);

        drawCards(handSize - cardsInHandBeforeShuffleCopy.size());

        getDiscard().addAll(copyCards(player.getDiscard()));

        getBases().addAll((Collection<? extends Base>) copyCards(player.getBases()));
        getInPlay().addAll(copyCards(player.getInPlay()));
        getPlayed().addAll(copyCards(player.getPlayed()));
        getHeroes().addAll((Collection<? extends Hero>) copyCards(player.getHeroes()));
        getGambits().addAll((Collection<? extends Gambit>) copyCards(player.getGambits()));

        shuffles = player.getShuffles();
        turn = player.getTurn();
        turns = player.getTurns();

        firstPlayer = player.isFirstPlayer();
    }

    private List<? extends Card> copyCards(List<? extends Card> cardsToCopy) {
        return cardsToCopy
                .stream()
                .map(Card::copyCardForSimulation)
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
        addGameLog(playerName + " drawing " + cards + " cards");
        for (int i = 0; i < cards; i++) {
            if (deck.isEmpty()) {
                cardsInHandBeforeShuffle.addAll(cardsDrawn);
                deck.addAll(discard);
                discard.clear();
                addGameLog("Shuffling deck");
                Collections.shuffle(deck);
                shuffles++;
            }

            if (!deck.isEmpty()) {
                Card cardToDraw = deck.remove(0);
                cardsDrawn.add(cardToDraw);
                addCardToHand(cardToDraw);
            }
        }

        if (yourTurn && deck.isEmpty()) {
            cardsInHandBeforeShuffle.addAll(cardsDrawn);
        }

        return cardsDrawn;
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
    }

    public void addCombat(int combat) {
        this.combat += combat;
    }

    public void addAuthority(int authority) {
        this.authority += authority;
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

        if (cardToBuyThisTurn != null && cardToBuyThisTurn instanceof DoNotBuyCard) {
            boughtSpecifiedCardOnFirstTurn = true;
        }

        cardToBuyThisTurn = null;

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

        factionsPlayedThisTurn.clear();

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
            ((ScrappableCard) card).cardScrapped(this);
        }
    }

    public void optionalScrapCardInTradeRow() {
        optionallyScrapCardsInTradeRow(1);
    }

    public abstract void optionallyScrapCardsInTradeRow(int cards);

    public void addCardToTopOfDeck(Card card) {
        if (card instanceof Hero) {
            heroes.add((Hero) card);
        } else {
            deck.add(0, card);
        }
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void cardAcquired(Card card) {
        Set<Card> cardsAcquiredInCurrentDeck = cardsAcquiredByDeck.get(getCurrentDeckNumber());
        if (cardsAcquiredInCurrentDeck == null) {
            cardsAcquiredInCurrentDeck = new HashSet<>();
        }
        cardsAcquiredInCurrentDeck.add(card);
        cardsAcquiredByDeck.put(getCurrentDeckNumber(), cardsAcquiredInCurrentDeck);

        if ((card instanceof ColonySeedShip && factionPlayedThisTurn(Faction.TRADE_FEDERATION)) ||
                (card instanceof EmperorsDreadnaught && factionPlayedThisTurn(Faction.STAR_EMPIRE)) ||
                (card instanceof PlasmaVent && blobCardPlayedThisTurn()) ||
                (card instanceof WarningBeacon && machineCultCardPlayedThisTurn())) {
            addCardToHand(card);
            addGameLog("Added " + card.getName() + " to hand");
        } else if (card instanceof Hero) {
            Hero hero = (Hero) card;
            heroes.add(hero);
            hero.heroAcquired(this);
        } else if (card.isShip() && (nextShipToTopOfDeck || nextShipOrBaseToTopOfDeck)) {
            nextShipToTopOfDeck = false;
            nextShipOrBaseToTopOfDeck = false;
            addCardToTopOfDeck(card);
        } else if (card.isBase() && (nextBaseToHand || nextShipOrBaseToHand)) {
            nextBaseToHand = false;
            nextShipOrBaseToHand = false;
            addCardToHand(card);
            addGameLog("Added " + card.getName() + " to hand");
        } else if (card.isBase() && nextShipOrBaseToTopOfDeck) {
            nextShipOrBaseToTopOfDeck = false;
            addCardToTopOfDeck(card);
        } else {
            discard.add(card);
        }

        if (cardToBuyThisTurn != null && cardToBuyThisTurn.equals(card)) {
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

    public boolean useAlliedAbilities(AlliableCard card) {
        Card cardToUse = (Card) card;

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
            if (allShipsAddOneCombat) {
                addCombat(1);
            }
            if (card.hasFaction(Faction.STAR_EMPIRE) && gainTwoCombatWhenStarEmpireShipPlayed) {
                addCombat(2);
            }
        }

        factionsPlayedThisTurn.addAll(card.getFactions());

        for (Card c : inPlay) {
            allyCardIfAvailable(c);
        }

        card.cardPlayed(this);
    }

    private void allyCardIfAvailable(Card card) {
        if (card.isAlliableCard()) {
            if (card.hasUnusedAllyAbility() && card.isAutoAlly()) {
                for (Faction faction : card.getFactions()) {
                    if (!card.getAutoAllyExcludedFactions().contains(faction)
                            && !card.isAlliedAbilityUsed(faction)
                            && cardHasAlly(card, faction)) {
                        card.setAlliedAbilityUsed(true, faction);
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

    public abstract void acquireFreeCardToHand(int maxCost);

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

    public void useHero(Hero hero) {
        addGameLog("Using hero " + hero.getName());
        heroes.remove(hero);
        playerCardScrapped(hero);
        hero.cardScrapped(this);
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
        yourTurn = true;
        turn++;
        addGameLog("");
        addGameLog("*** " + playerName + "'s Turn " + turn + " ***");

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

    public void setCardToBuyThisTurn(Card cardToBuyThisTurn) {
        this.cardToBuyThisTurn = cardToBuyThisTurn;
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
}
