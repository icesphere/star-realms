package org.smartreaction.starrealms.model.cards.heroes.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.players.Player;

public class CommanderZhang extends Hero {
    public CommanderZhang() {
        name = "Commander Zhang";
        set = CardSet.UNITED_HEROES;
        cost = 5;
        text = "Buy: Until end of turn, you may use all of your Star Empire Ally abilities. Add 4 Combat; Scrap: Until end of turn, you may use all of your Star Empire Ally abilities. Draw a card.";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.STAR_EMPIRE;
    }

    @Override
    public void heroBought(Player player) {
        player.starEmpireAlliedUntilEndOfTurn();
        player.addCombat(4);
    }

    @Override
    public void cardScrapped(Player player) {
        player.starEmpireAlliedUntilEndOfTurn();
        player.drawCard();
    }
}
