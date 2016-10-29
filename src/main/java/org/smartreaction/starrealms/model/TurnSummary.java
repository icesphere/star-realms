package org.smartreaction.starrealms.model;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.bases.Base;

import java.util.ArrayList;
import java.util.List;

public class TurnSummary {
    private List<Card> cardsAcquired = new ArrayList<>();

    private List<Base> opponentBasesDestroyed = new ArrayList<>();

    private int damageToOpponent;

    private int authorityGained;

    private int gameTurn;

    private List<Card> cardsScrapped = new ArrayList<>();

    private List<Card> cardsPlayed = new ArrayList<>();

    private List<Card> cardsAddedToTradeRow = new ArrayList<>();

    public List<Card> getCardsAcquired() {
        return cardsAcquired;
    }

    public void setCardsAcquired(List<Card> cardsAcquired) {
        this.cardsAcquired = cardsAcquired;
    }

    public List<Base> getOpponentBasesDestroyed() {
        return opponentBasesDestroyed;
    }

    public void setOpponentBasesDestroyed(List<Base> opponentBasesDestroyed) {
        this.opponentBasesDestroyed = opponentBasesDestroyed;
    }

    public int getDamageToOpponent() {
        return damageToOpponent;
    }

    public void setDamageToOpponent(int damageToOpponent) {
        this.damageToOpponent = damageToOpponent;
    }

    public int getAuthorityGained() {
        return authorityGained;
    }

    public void setAuthorityGained(int authorityGained) {
        this.authorityGained = authorityGained;
    }

    public int getGameTurn() {
        return gameTurn;
    }

    public void setGameTurn(int gameTurn) {
        this.gameTurn = gameTurn;
    }

    public List<Card> getCardsScrapped() {
        return cardsScrapped;
    }

    public void setCardsScrapped(List<Card> cardsScrapped) {
        this.cardsScrapped = cardsScrapped;
    }

    public List<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public void setCardsPlayed(List<Card> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
    }

    public List<Card> getCardsAddedToTradeRow() {
        return cardsAddedToTradeRow;
    }

    public void setCardsAddedToTradeRow(List<Card> cardsAddedToTradeRow) {
        this.cardsAddedToTradeRow = cardsAddedToTradeRow;
    }
}
