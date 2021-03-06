package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.Arrays;
import java.util.List;

public class ChoiceAction extends Action {
    private ChoiceActionCard card;

    private List<Choice> choices;

    public ChoiceAction(ChoiceActionCard card, Choice... choices) {
        this.card = card;
        this.choices = Arrays.asList(choices);
        this.text = "";
    }

    public ChoiceAction(ChoiceActionCard card, String text, Choice... choices) {
        this.card = card;
        this.choices = Arrays.asList(choices);
        this.text = text;
    }

    @Override
    public List<Choice> getChoices() {
        return choices;
    }

    @Override
    public boolean isCardActionable(Card card, String cardLocation, Player player) {
        return false;
    }

    @Override
    public boolean processAction(Player player) {
        return true;
    }

    @Override
    public boolean processActionResult(Player player, ActionResult result) {
        card.actionChoiceMade(player, result.getChoiceSelected());
        return true;
    }
}
