package org.smartreaction.starrealms.model.cards.scenarios;

import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.players.Player;

import java.util.Random;

public class EntrenchedLoyalties extends Scenario implements StartOfGameScenario {

    public EntrenchedLoyalties() {
        super("Entrenched Loyalties", "At the start of the game, assign each player a faction at random. Cards of that faction cost that player 1 trade less to acquire. (The same faction may be assigned to multiple players.)");
    }

    @Override
    public void applyScenarioToGame(Game game) {
        for (Player player : game.getPlayers()) {
            player.setEntrenchedLoyaltyFaction(getRandomFaction());

            addText(" Loyalty faction for " + player.getPlayerName() + ": " + getFactionName(player.getEntrenchedLoyaltyFaction()) + ".");

            player.setCardCostModifier((c, p) -> {
                if (c.hasFaction(p.getEntrenchedLoyaltyFaction())) {
                    return c.getCost() - 1;
                }

                return c.getCost();
            });
        }
    }

    Faction getRandomFaction() {
        int randomInt = new Random().nextInt(4);

        switch (randomInt) {
            case 0:
                return Faction.BLOB;
            case 1:
                return Faction.MACHINE_CULT;
            case 2:
                return Faction.STAR_EMPIRE;
            case 3:
                return Faction.TRADE_FEDERATION;
            default:
                throw new IllegalStateException("invalid random int for random faction");
        }
    }

    String getFactionName(Faction faction) {

        switch (faction) {
            case BLOB:
                return "Blob";
            case MACHINE_CULT:
                return "Machine Cult";
            case STAR_EMPIRE:
                return "Star Empire";
            case TRADE_FEDERATION:
                return "Trade Federation";
            default:
                throw new IllegalArgumentException("Invalid faction: " + faction.toString());
        }

    }
}
