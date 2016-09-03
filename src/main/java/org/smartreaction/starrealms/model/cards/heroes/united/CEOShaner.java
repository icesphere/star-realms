package org.smartreaction.starrealms.model.cards.heroes.united;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.heroes.Hero;
import org.smartreaction.starrealms.model.players.Player;

public class CEOShaner extends Hero {
    public CEOShaner() {
        name = "CEO Shaner";
        set = CardSet.UNITED_HEROES;
        cost = 5;
        text = "Buy: Until end of turn, you may use all of your Trade Federation Ally abilities. You may acquire a ship or base of cost 3 or less and put it on top of your deck; Scrap: Until end of turn, you may use all of your Trade Federation Ally abilities. Draw a card.";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.TRADE_FEDERATION;
    }

    @Override
    public void heroAcquired(Player player) {
        player.tradeFederationAlliedUntilEndOfTurn();
        player.acquireFreeShipToTopOfDeck(3);
    }

    @Override
    public void cardScrapped(Player player) {
        player.tradeFederationAlliedUntilEndOfTurn();
        player.drawCard();
    }
}
