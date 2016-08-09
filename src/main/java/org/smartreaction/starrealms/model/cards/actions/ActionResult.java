package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.cards.Card;

public class ActionResult {
    private Card selectedCard;

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
        return selectedCard;
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
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
