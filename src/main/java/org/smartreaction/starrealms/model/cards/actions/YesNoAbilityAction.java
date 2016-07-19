package org.smartreaction.starrealms.model.cards.actions;

import org.smartreaction.starrealms.model.Choice;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.players.Player;

import java.util.ArrayList;
import java.util.List;

public class YesNoAbilityAction extends Action {
    private ChoiceActionCard card;

    public YesNoAbilityAction(ChoiceActionCard card, String text) {
        this.card = card;
        this.text = text;
    }

    @Override
    public List<Choice> getChoices() {
        List<Choice> choices = new ArrayList<>();

        Choice yes = new Choice(1, "Yes");
        Choice no = new Choice(2, "No");

        choices.add(yes);
        choices.add(no);

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
    public void processActionResult(Player player, ActionResult result) {
        card.actionChoiceMade(player, result.getChoiceSelected());
    }
}
