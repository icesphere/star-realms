package org.smartreaction.starrealms.model.cards;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.outposts.Outpost;
import org.smartreaction.starrealms.model.cards.gambits.Gambit;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.cards.ships.Scout;
import org.smartreaction.starrealms.model.cards.ships.Ship;
import org.smartreaction.starrealms.model.cards.ships.Viper;
import org.smartreaction.starrealms.model.players.Player;

import java.util.*;

public abstract class Card {
    protected String id;
    protected String name;
    protected Set<Faction> factions = new HashSet<>(1);
    protected int cost;
    protected CardSet set;
    protected String text;
    protected int shield;
    protected Map<Faction, Boolean> alliedAbilityUsed = new HashMap<>(4);
    protected boolean autoAlly = true;
    protected List<Faction> autoAllyExcludedFactions = new ArrayList<>(0);
    protected boolean allFactionsAlliedTogether;
    protected boolean copied;

    public static String CARD_LOCATION_HAND = "hand";
    public static String CARD_LOCATION_PLAY_AREA = "playArea";
    public static String CARD_LOCATION_DECK = "deck";
    public static String CARD_LOCATION_DISCARD = "discard";
    public static String CARD_LOCATION_PLAYER_BASES = "playerBases";
    public static String CARD_LOCATION_OPPONENT_BASES = "opponentBases";
    public static String CARD_LOCATION_PLAYER_HEROES = "playerHeroes";
    public static String CARD_LOCATION_OPPONENT_HEROES = "opponentHeroes";
    public static String CARD_LOCATION_PLAYER_GAMBITS = "playerGambits";
    public static String CARD_LOCATION_OPPONENT_GAMBITS = "opponentGambits";
    public static String CARD_LOCATION_TRADE_ROW = "tradeRow";
    public static String CARD_LOCATION_EXPLORERS = "explorers";

    protected Card() {
        id = UUID.randomUUID().toString();
        alliedAbilityUsed.put(Faction.BLOB, false);
        alliedAbilityUsed.put(Faction.MACHINE_CULT, false);
        alliedAbilityUsed.put(Faction.TRADE_FEDERATION, false);
        alliedAbilityUsed.put(Faction.STAR_EMPIRE, false);
    }

    public void resetCard() {
        setAllAlliedAbilitiesToNotUsed();
        allFactionsAlliedTogether = false;
        copied = false;
    }

    public void resetTo(Card card) {
        for (Faction faction : alliedAbilityUsed.keySet()) {
            setAlliedAbilityUsed(card.isAlliedAbilityUsed(faction), faction);
        }
        allFactionsAlliedTogether = card.isAllFactionsAlliedTogether();
        copied = card.isCopied();
    }

    public Card copyCardForSimulation() {
        Card card;
        try {
            card = this.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to copy card");
        }

        Map<Faction, Boolean> alliedAbilityUsedCopy = new HashMap<>();
        for (Faction faction : alliedAbilityUsed.keySet()) {
            alliedAbilityUsedCopy.put(faction, isAlliedAbilityUsed(faction));
        }
        card.setAlliedAbilityUsed(alliedAbilityUsedCopy);

        card.setFactions(new HashSet<>(getFactions()));

        card.setAutoAllyExcludedFactions(new ArrayList<>(getAutoAllyExcludedFactions()));

        card.setCost(getCost());
        card.setSet(getSet());
        card.setText(getText());
        card.setShield(getShield());
        card.setAutoAlly(isAutoAlly());
        card.setAllFactionsAlliedTogether(isAllFactionsAlliedTogether());
        card.setCopied(isCopied());

        return card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFaction(Faction faction) {
        getFactions().add(faction);
    }

    public boolean hasFaction(Faction faction) {
        return getFactions().contains(faction);
    }

    public Set<Faction> getFactions() {
        return factions;
    }

    private void setFactions(Set<Faction> factions) {
        this.factions = factions;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public CardSet getSet() {
        return set;
    }

    public void setSet(CardSet set) {
        this.set = set;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public boolean isScrappable() {
        return this instanceof ScrappableCard;
    }

    public boolean isBase() {
        return this instanceof Base;
    }

    public boolean isHero() {
        return this instanceof Hero;
    }

    public boolean isOutpost() {
        return this instanceof Outpost;
    }

    public boolean isShip() {
        return this instanceof Ship;
    }

    public boolean isStarterCard() {
        return this instanceof Scout || this instanceof Viper;
    }

    public abstract void cardPlayed(Player player);

    public boolean isAlliedAbilityUsed(Faction faction) {
        return alliedAbilityUsed.get(faction);
    }

    public void setAlliedAbilityUsed(boolean used, Faction faction) {
        if (used && isAllFactionsAlliedTogether()) {
            for (Faction f : getFactions()) {
                alliedAbilityUsed.put(f, true);
            }
        } else {
            alliedAbilityUsed.put(faction, used);
        }
    }

    private void setAlliedAbilityUsed(Map<Faction, Boolean> alliedAbilityUsed) {
        this.alliedAbilityUsed = alliedAbilityUsed;
    }

    public void setAllAlliedAbilitiesToNotUsed() {
        for (Faction faction : alliedAbilityUsed.keySet()) {
            setAlliedAbilityUsed(false, faction);
        }
    }

    public void removedFromPlay(Player player) {}

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final Card other = (Card) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public List<Faction> getAlliedFactions(Card card) {
        List<Faction> alliedFactions = new ArrayList<>();
        for (Faction faction : card.getFactions()) {
            if (getFactions().contains(faction)) {
                alliedFactions.add(faction);
            }
        }
        return alliedFactions;
    }

    public int getCombatWhenScrapped() {
        return 0;
    }

    public boolean canDestroyBasedWhenScrapped() {
        return false;
    }

    public int getAuthorityWhenScrapped() {
        return 0;
    }

    public int getTradeWhenScrapped() {
        return 0;
    }

    public int getTradeWhenPlayed() {
        return 0;
    }

    public abstract boolean isActionable(Player player, String cardLocation);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAutoAlly() {
        return autoAlly;
    }

    public void setAutoAlly(boolean autoAlly) {
        this.autoAlly = autoAlly;
    }

    public boolean isGambit() {
        return this instanceof Gambit;
    }

    public boolean hasUnusedAllyAbility() {
        for (Faction faction : getFactions()) {
            if (!alliedAbilityUsed.get(faction)) {
                return true;
            }
        }
        return false;
    }

    public void addAutoAllyExcludedFaction(Faction faction) {
        autoAllyExcludedFactions.add(faction);
    }

    public List<Faction> getAutoAllyExcludedFactions() {
        return autoAllyExcludedFactions;
    }

    private void setAutoAllyExcludedFactions(List<Faction> autoAllyExcludedFactions) {
        this.autoAllyExcludedFactions = autoAllyExcludedFactions;
    }

    public boolean isAllFactionsAlliedTogether() {
        return allFactionsAlliedTogether;
    }

    private void setAllFactionsAlliedTogether(boolean allFactionsAlliedTogether) {
        this.allFactionsAlliedTogether = allFactionsAlliedTogether;
    }

    public boolean isCopied() {
        return copied;
    }

    public void setCopied(boolean copied) {
        this.copied = copied;
    }

    public boolean isAlliableCard() {
        return this instanceof AlliableCard;
    }

    public AlliableCard getAlliableCard() {
        if (this instanceof AlliableCard) {
            return (AlliableCard) this;
        }
        return null;
    }

    public boolean isScrapper() {
        return false;
    }
}
