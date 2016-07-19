package org.smartreaction.starrealms.model.players;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.Game;
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
import org.smartreaction.starrealms.model.cards.gambits.EveryTurnGambit;
import org.smartreaction.starrealms.model.cards.gambits.Gambit;
import org.smartreaction.starrealms.model.cards.gambits.ScrappableGambit;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
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

    private Map<Integer, Set<Card>> cardsAcquiredByDeck = new HashMap<>();

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

    private int shuffles;

    private boolean firstPlayer;

    protected String playerName;

    private int turns;

    protected int turn;

    private boolean blobAlliedUntilEndOfTurn;
    private boolean starEmpireAlliedUntilEndOfTurn;
    private boolean tradeFederationAlliedUntilEndOfTurn;
    private boolean machineCultAlliedUntilEndOfTurn;

    private boolean gainTwoCombatWhenStarEmpireShipPlayed;

    private Set<Faction> factionsPlayedThisTurn = new HashSet<>();

    protected Comparator<Base> baseShieldAscending = (b1, b2) -> Integer.compare(b1.getShield(), b2.getShield());
    protected Comparator<Base> baseShieldDescending = baseShieldAscending.reversed();

    protected Player() {
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
        getGame().gameLog(playerName + " drawing " + cards + " cards");
        for (int i = 0; i < cards; i++) {
            if (deck.isEmpty()) {
                deck.addAll(discard);
                discard.clear();
                getGame().gameLog("Shuffling deck");
                Collections.shuffle(deck);
                shuffles++;
            }

            if (!deck.isEmpty()) {
                Card cardToDraw = deck.remove(0);
                cardsDrawn.add(cardToDraw);
                addCardToHand(cardToDraw);
                getGame().gameLog("Added " + cardToDraw.getName() + " to hand");
            }
        }

        return cardsDrawn;
    }

    public void discardCard(Card card) {
        getGame().gameLog("Discarded " + card.getName());
        hand.remove(card);
        discard.add(card);
        cardRemovedFromPlay(card);
    }

    public void opponentDiscardsCard() {
        getGame().gameLog("Opponent discarding card");
        opponent.discardCardFromHand();
    }

    private void cardRemovedFromPlay(Card card) {
        card.setAlliedAbilityUsed(false);

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
    }

    public void endTurn() {
        getGame().gameLog("Ending turn");

        turns++;

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

        factionsPlayedThisTurn.clear();

        played.clear();

        for (Card card : inPlay) {
            if (card.isBase()) {
                ((Base) card).setUsed(false);
                card.setAlliedAbilityUsed(false);
            } else {
                discard.add(card);
                cardRemovedFromPlay(card);
            }
        }

        inPlay.clear();

        discard.addAll(hand);
        hand.clear();

        drawCards(5);

        yourTurn = false;

        game.turnEnded();
    }

    public void addBase(Base base) {
        this.getBases().add(base);
    }

    public void destroyTargetBase() {
        addAction(new DestroyOpponentBase("Destroy target base"));
    }

    public void baseDestroyed(Base base) {
        getGame().gameLog("Destroyed base: " + base.getName());
        bases.remove(base);
        discard.add(base);
        cardRemovedFromPlay(base);
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

    public void optionallyScrapCardsFromHandOrDiscard(int cards) {
        addAction(new ScrapCardsFromHandOrDiscardPile(cards, "Scrap up to " + cards + " from your hand or discard pile"));
    }

    public void optionallyScrapCardsFromHandOrDiscardForBenefit(ScrapCardsForBenefitActionCard card, int numCardsToScrap) {
        addAction(new ScrapCardsFromHandOrDiscardPileForBenefit(card, numCardsToScrap, "Scrap up to " + numCardsToScrap + " from your hand or discard pile"));
    }

    public void scrapCardFromDiscard(Card card) {
        getGame().gameLog("Scrapped " + card.getName() + " from discard");
        discard.remove(card);
        playerCardScrapped(card);
    }

    public void scrapCardFromHand(Card card) {
        getGame().gameLog("Scrapped " + card.getName() + " from hand");
        hand.remove(card);
        playerCardScrapped(card);
    }

    private void playerCardScrapped(Card card) {
        if (!card.isStarterCard() && card instanceof Explorer) {
            game.getTradeRowCardsScrapped().add(card);
        }
        cardRemovedFromPlay(card);
    }

    public void scrapCardInPlayForBenefit(Card card) {
        if (card.isScrappable()) {
            getGame().gameLog("Scrapped " + card.getName() + " from in play for benefit");
            inPlay.remove(card);
            if (card instanceof Base) {
                bases.remove(card);
            }
            playerCardScrapped(card);
            ((ScrappableCard) card).cardScrapped(this);
        }
    }

    public void scrapCardInTradeRow() {
        scrapCardsInTradeRow(1);
    }

    public void scrapCardsInTradeRow(int cards) {
        addAction(new ScrapCardsFromTradeRow(cards));
    }

    public void acquireFreeShipAndPutOnTopOfDeck() {
        addAction(new FreeCardFromTradeRowToTopOfDeck(null, "Choose a free ship from the trade row to put on top of your deck"));
    }

    public void addCardToTopOfDeck(Card card) {
        if (card instanceof Hero) {
            heroes.add((Hero) card);
        } else {
            deck.add(0, card);
            getGame().gameLog(card.getName() + " added to top of deck");
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
            getGame().gameLog("Added " + card.getName() + " to hand");
        } else if (card instanceof Hero) {
            heroes.add((Hero) card);
        } else if (card.isShip() && (nextShipToTopOfDeck || nextShipOrBaseToTopOfDeck)) {
            nextShipToTopOfDeck = false;
            nextShipOrBaseToTopOfDeck = false;
            addCardToTopOfDeck(card);
        } else if (card.isBase() && (nextBaseToHand || nextShipOrBaseToHand)) {
            nextBaseToHand = false;
            nextShipOrBaseToHand = false;
            addCardToHand(card);
            getGame().gameLog("Added " + card.getName() + " to hand");
        } else if (card.isBase() && nextShipOrBaseToTopOfDeck) {
            nextShipOrBaseToTopOfDeck = false;
            addCardToTopOfDeck(card);
        } else {
            discard.add(card);
        }
    }

    public void makeChoice(ChoiceActionCard card, Choice... choices) {
        addAction(new ChoiceAction(card, choices));
    }

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

    public void scrapCardFromHand(boolean optional) {
        if (optional) {
            addAction(new ScrapCardsFromHand(1, "You may scrap a card from your hand"));
        } else {
            addAction(new ScrapCardFromHand("Scrap a card from your hand"));
        }
    }

    public void optionallyScrapCardsFromDiscard(int cards) {
        addAction(new ScrapCardsFromDiscardPile(cards, "You may scrap a card from your discard pile"));
    }

    public void setAllFactionsAllied(boolean allFactionsAllied) {
        this.allFactionsAllied = allFactionsAllied;
    }

    public void buyCard(Card card) {
        if (trade >= card.getCost()) {
            getGame().gameLog("Bought card: " + card.getName());
            trade -= card.getCost();
            if (card instanceof Explorer) {
                cardAcquired(new Explorer());
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
        getGame().gameLog("Applied " + combat + " combat to opponent");
        if (opponent.isPreventFirstDamage() && combat > 0) {
            opponent.reduceAuthority(combat - 1);
            opponent.setPreventFirstDamage(false);
        } else {
            opponent.reduceAuthority(combat);
        }
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

        return cards;
    }

    public int getNumBases() {
        return countCardsByType(getAllCards(), Card::isBase);
    }

    public boolean useAlliedAbility(AlliableCard card) {
        Card cardToUse = (Card) card;

        if (card instanceof StealthNeedle && ((StealthNeedle) card).getCardBeingCopied() != null) {
            cardToUse = ((StealthNeedle) card).getCardBeingCopied();
        }
        if (card instanceof StealthTower && ((StealthTower) card).getBaseBeingCopied() != null) {
            cardToUse = ((StealthTower) card).getBaseBeingCopied();
        }
        if (!cardToUse.isAlliedAbilityUsed() && cardHasAlly(cardToUse)) {
            getGame().gameLog("Using allied ability of " + cardToUse.getName());
            cardToUse.setAlliedAbilityUsed(true);
            ((AlliableCard) cardToUse).cardAllied(this);
            return true;
        }

        return false;
    }

    public boolean cardHasAlly(Card card) {
        if (allFactionsAllied) {
            game.gameLog("All factions allied");
            return true;
        }

        if (card.isBlob() && blobAlliedUntilEndOfTurn) {
            return true;
        }

        if (card.isStarEmpire() && starEmpireAlliedUntilEndOfTurn) {
            return true;
        }

        if (card.isTradeFederation() && tradeFederationAlliedUntilEndOfTurn) {
            return true;
        }

        if (card.isMachineCult() && machineCultAlliedUntilEndOfTurn) {
            return true;
        }

        for (Card c : inPlay) {
            if (c != card && c.isAlly(card)) {
                return true;
            }
        }

        return false;
    }

    public void playCard(Card card) {
        game.gameLog("Played card: " + card.getName());
        played.add(card);
        inPlay.add(card);
        hand.remove(card);

        if (card.isBase()) {
            addBase((Base) card);
        }

        if (card.isShip()) {
            if (allShipsAddOneCombat) {
                addCombat(1);
            }
            if (card.isStarEmpire() && gainTwoCombatWhenStarEmpireShipPlayed) {
                addCombat(2);
            }
        }

        factionsPlayedThisTurn.add(card.getFaction());

        card.cardPlayed(this);
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

    public void returnTargetBaseToHand() {
        addAction(new ReturnBaseToHand("Return a base to owner's hand"));
    }

    public int getCurrentDeckNumber() {
        return getShuffles() + 1;
    }

    public void addCardFromDiscardToTopOfDeck() {
        Card card = chooseCardFromDiscardToAddToTopOfDeck();
        if (card != null) {
            discard.remove(card);
            addCardToTopOfDeck(card);
        }
    }

    public abstract Card chooseCardFromDiscardToAddToTopOfDeck();

    public boolean isPreventFirstDamage() {
        return preventFirstDamage;
    }

    public void setPreventFirstDamage(boolean preventFirstDamage) {
        this.preventFirstDamage = preventFirstDamage;
    }

    public void acquireFreeCard(int maxCost) {
        addAction(new FreeCardFromTradeRow(maxCost, "Acquire a free card from the trade row costing up to " + maxCost));
    }

    public void acquireFreeCardToTopOfDeck(int maxCost) {
        addAction(new FreeCardFromTradeRowToTopOfDeck(maxCost, "Acquire a free card from the trade row to the top of your deck costing up to " + maxCost));
    }

    public void acquireFreeCardToHand(int maxCost) {
        addAction(new FreeCardFromTradeRowToHand(maxCost, "Acquire a free card from the trade row to your hand costing up to " + maxCost));
    }

    public List<Gambit> getGambits() {
        return gambits;
    }

    public List<Gambit> getScrappableGambits() {
        return gambits.stream().filter(g -> g instanceof ScrappableGambit).collect(toList());
    }

    public List<Gambit> getEveryTurnGambits() {
        return gambits.stream().filter(g -> g instanceof EveryTurnGambit).collect(toList());
    }

    public void setGambits(List<Gambit> gambits) {
        this.gambits = gambits;
    }

    public void gambitScrapped(Gambit gambit) {
        getGame().gameLog("Scrapped gambit " + gambit.getName());
        this.getGambits().remove(gambit);
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

    public int getTurns() {
        return turns;
    }

    public Faction getFactionWithMostCards() {
        List<Card> cards = getAllCards();

        Map<Faction, Integer> factionCounts = new HashMap<>(4);

        factionCounts.put(Faction.BLOB, countCardsByType(cards, Card::isBlob));
        factionCounts.put(Faction.STAR_EMPIRE, countCardsByType(cards, Card::isStarEmpire));
        factionCounts.put(Faction.TRADE_FEDERATION, countCardsByType(cards, Card::isTradeFederation));
        factionCounts.put(Faction.MACHINE_CULT, countCardsByType(cards, Card::isMachineCult));

        Faction factionWithMostCards = null;
        int highestFactionCount = 0;

        for (Faction faction : factionCounts.keySet()) {
            if (factionCounts.get(faction) >= highestFactionCount) {
                factionWithMostCards = faction;
            }
        }

        return factionWithMostCards;
    }

    public abstract void handleBlackHole();

    public abstract void handleBombardment();

    public abstract void drawCardsAndPutSomeBackOnTop(int cardsToDraw, int cardsToPutBack);

    public abstract void handleDeathWorld();

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
        getGame().gameLog("Using hero " + hero.getName());
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

    public void discardCards(List<Card> cards) {
        for (Card card : cards) {
            discardCard(card);
        }
    }

    public boolean blobCardPlayedThisTurn() {
        return cardPlayedThisTurn(Card::isBlob);
    }

    public boolean tradeFederationCardPlayedThisTurn() {
        return cardPlayedThisTurn(Card::isTradeFederation);
    }

    public boolean starEmpireCardPlayedThisTurn() {
        return cardPlayedThisTurn(Card::isStarEmpire);
    }

    public boolean machineCultCardPlayedThisTurn() {
        return cardPlayedThisTurn(Card::isMachineCult);
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
        return countCardsByType(played, Card::isBlob);
    }

    public List getHandAndDeck() {
        List<Card> cards = new ArrayList<>(hand);
        cards.addAll(deck);
        return cards;
    }

    public void addGameLog(String log) {
        getGame().gameLog(log);
    }

    public void addCardToDiscard(Card card) {
        discard.add(card);
    }

    public void discardCardFromHand() {
        discardCardsFromHand(1);
    }

    public void discardCardFromHand(Card card) {
        hand.remove(card);
        addCardToDiscard(card);
        addGameLog(playerName + " discarded " + card.getName() + " from hand");
    }

    public void discardCardsFromHand(int cards) {
        String text = "Discard " + cards + " card";
        if (cards != 1) {
            text += "s";
        }
        addAction(new DiscardCardsFromHand(cards, text));
    }

    public void addAction(Action action) {
        actionsQueue.add(action);
        if (yourTurn && currentAction == null) {
            resolveActions();
        }
    }

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
        if (!result.getSelectedCards().isEmpty() || result.getChoiceSelected() != null) {
            action.processActionResult(this, result);
        }

        currentAction = null;

        resolveActions();
    }

    public void playAll() {
        List<Card> copyOfHand = new ArrayList<>(hand);
        copyOfHand.forEach(this::playCard);
    }

    public void startTurn() {
        yourTurn = true;
        turn++;
        addGameLog("** " + playerName + "'s Turn " + turn + " **");

        inPlay.addAll(bases);

        resolveActions();
    }
}
