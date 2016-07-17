package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class ActionResult {
    private List<Card> selectedCards = new ArrayList<>(3);

    private Integer choiceSelected;

    private String cardLocation;

    public ActionResult() {
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
    }

    public void setSelectedCards(List<Card> selectedCards) {
        this.selectedCards = selectedCards;
    }

    public Integer getChoiceSelected() {
        return choiceSelected;
    }

    public void setChoiceSelected(Integer choiceSelected) {
        this.choiceSelected = choiceSelected;
    }

    public String getCardLocation() {
        return cardLocation;
    }

    public void setCardLocation(String cardLocation) {
        this.cardLocation = cardLocation;
    }

    public Card getSelectedCard() {
        if (selectedCards.size() > 0) {
            return selectedCards.get(0);
        }
        return null;
    }
}
