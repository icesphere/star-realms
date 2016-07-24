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

import java.util.Objects;
import java.util.UUID;

public abstract class Card {
    protected String id;
    protected String name;
    protected Faction faction;
    protected int cost;
    protected CardSet set;
    protected String text;
    protected int shield;
    protected boolean alliedAbilityUsed;
    protected boolean autoAlly = true;

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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
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

    public boolean isBlob() {
        return this.faction == Faction.BLOB;
    }

    public boolean isTradeFederation() {
        return this.faction == Faction.TRADE_FEDERATION;
    }

    public boolean isMachineCult() {
        return this.faction == Faction.MACHINE_CULT;
    }

    public boolean isStarEmpire() {
        return this.faction == Faction.STAR_EMPIRE;
    }

    public boolean isStarterCard() {
        return this instanceof Scout || this instanceof Viper;
    }

    public abstract void cardPlayed(Player player);

    public boolean isAlliedAbilityUsed() {
        return alliedAbilityUsed;
    }

    public void setAlliedAbilityUsed(boolean alliedAbilityUsed) {
        this.alliedAbilityUsed = alliedAbilityUsed;
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

    public boolean isAlly(Card card) {
        return card.getFaction() == this.getFaction();
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
}
