package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class ActionResult {
    private List<Card> selectedCards = new ArrayList<>(3);

    private Integer choiceSelected;

    private String cardLocation;

    private boolean doNotUse;

    private boolean doneWithAction;

    public ActionResult() {
    }

    public static ActionResult doNotUseActionResult() {
        ActionResult actionResult = new ActionResult();
        actionResult.setDoNotUse(true);
        return actionResult;
    }

    public static ActionResult doneWithActionResult() {
        ActionResult actionResult = new ActionResult();
        actionResult.setDoneWithAction(true);
        return actionResult;
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
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

    public boolean isDoNotUse() {
        return doNotUse;
    }

    public void setDoNotUse(boolean doNotUse) {
        this.doNotUse = doNotUse;
    }

    public boolean isDoneWithAction() {
        return doneWithAction;
    }

    public void setDoneWithAction(boolean doneWithAction) {
        this.doneWithAction = doneWithAction;
    }
}
