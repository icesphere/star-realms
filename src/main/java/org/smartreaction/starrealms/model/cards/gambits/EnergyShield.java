package org.smartreaction.starrealms.model.cards.gambits;

import org.smartreaction.starrealms.model.players.Player;

public class EnergyShield extends Gambit implements ScrappableGambit, EveryTurnGambit {
    public EnergyShield() {
        name = "Energy Shield";
    }

    @Override
    public void everyTurnAbility(Player player) {
        player.setPreventFirstDamage(true);
    }

    @Override
    public void scrapGambit(Player player) {
        player.setPreventFirstDamage(false);
        player.drawCard();
    }
}
