package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.cards.ScrappableCard;
import org.smartreaction.starrealms.model.players.Player;

public class EnergyShield extends Gambit implements EveryTurnGambit, ScrappableCard {
    public EnergyShield() {
        name = "Energy Shield";
        text = "Whenever you (not your bases) are attacked, reduce the damage by 1; Scrap: Draw a card";
    }

    @Override
    public void everyTurnAbility(Player player) {
        player.setPreventFirstDamage(true);
    }

    @Override
    public void cardScrapped(Player player) {
        player.setPreventFirstDamage(false);
        player.drawCard();
    }
}
