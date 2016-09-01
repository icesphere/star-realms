package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.blob.*;
import org.smartreaction.starrealms.model.cards.bases.outposts.machinecult.*;
import org.smartreaction.starrealms.model.cards.bases.outposts.starempire.*;
import org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation.*;
import org.smartreaction.starrealms.model.cards.bases.starempire.FleetHQ;
import org.smartreaction.starrealms.model.cards.bases.starempire.OrbitalPlatform;
import org.smartreaction.starrealms.model.cards.bases.starempire.StarbaseOmega;
import org.smartreaction.starrealms.model.cards.bases.tradefederation.*;
import org.smartreaction.starrealms.model.cards.heroes.*;
import org.smartreaction.starrealms.model.cards.heroes.united.*;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.cards.ships.MercCruiser;
import org.smartreaction.starrealms.model.cards.ships.blob.*;
import org.smartreaction.starrealms.model.cards.ships.machinecult.*;
import org.smartreaction.starrealms.model.cards.ships.starempire.*;
import org.smartreaction.starrealms.model.cards.ships.tradefederation.*;
import org.smartreaction.starrealms.model.cards.ships.united.*;
import org.smartreaction.starrealms.model.players.Player;

public class DefenseVelocityStrategy implements BotStrategy {
    @Override
    public int getBuyCardScore(Card card, Player player) {
        int deck = player.getCurrentDeckNumber();
        int bases = player.getNumBasesInAllCards();
        Player opponent = player.getOpponent();
        int opponentBases = opponent.getNumBasesInAllCards();

        //Heroes
        if (card instanceof RamPilot) {
            if (deck == 1) {
                return 1;
            } else if (deck <= 3) {
                return 15;
            }
            return 5;
        } else if (card instanceof BlobOverlord) {
            if (deck == 1) {
                return 5;
            } else if (deck <= 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof SpecialOpsDirector) {
            if (deck == 1) {
                return 5;
            } else if (deck <= 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof CeoTorres) {
            if (deck == 1) {
                return 10;
            } else if (deck <= 3) {
                return 40;
            }
            return 25;
        } else if (card instanceof WarElder) {
            if (deck == 1) {
                return 10;
            } else if (deck <= 3) {
                return 25;
            }
            return 5;
        } else if (card instanceof HighPriestLyle) {
            if (deck == 1) {
                return 15;
            } else if (deck <= 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof CunningCaptain) {
            if (deck == 1) {
                return 1;
            } else if (deck <= 3) {
                return 10;
            }
            return 4;
        } else if (card instanceof AdmiralRasmussen) {
            if (deck == 1) {
                return 4;
            } else if (deck <= 3) {
                return 15;
            }
            return 5;
        }

        //todo update scores
        else if (card instanceof CEOShaner) {
            if (deck <= 3) {
                return 55;
            }
            return 35;
        } else if (card instanceof ChairmanHaygan) {
            if (deck == 1) {
                return 20;
            } else if (deck <= 3) {
                return 30;
            }
            return 25;
        } else if (card instanceof ChancellorHartman) {
            if (deck <= 3) {
                return 55;
            }
            return 25;
        } else if (card instanceof ConfessorMorris) {
            if (deck <= 3) {
                return 65;
            }
            return 40;
        } else if (card instanceof CommanderKlik) {
            if (deck == 1) {
                return 15;
            } else if (deck <= 3) {
                return 35;
            }
            return 15;
        } else if (card instanceof CommanderZhang) {
            if (deck == 1) {
                return 25;
            } else if (deck <= 3) {
                return 45;
            }
            return 50;
        } else if (card instanceof Screecher) {
            if (deck == 1) {
                return 10;
            } else if (deck <= 3) {
                return 35;
            }
            return 15;
        } else if (card instanceof HiveLord) {
            if (deck == 1) {
                return 10;
            } else if (deck <= 3) {
                return 35;
            }
            return 40;
        }

        //United
        if (card instanceof AllianceFrigate) {
            if (deck < 3) {
                return 5;
            }
            return 15;
        } else if (card instanceof AllianceTransport) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof AssaultPod) {
            if (deck == 1) {
                return 4;
            } else if (deck == 2) {
                return 10;
            }
            return 25;
        } else if (card instanceof BlobBot) {
            if (deck <= 2) {
                return 35;
            } else if (deck == 3) {
                return 25;
            }
            return 10;
        } else if (card instanceof CoalitionFreighter) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 50;
            } else if (deck == 3) {
                return 20;
            }
        } else if (card instanceof CoalitionMessenger) {
            if (deck == 1) {
                return 45;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 10;
            }
        } else if (card instanceof TradeStar) {
            if (deck == 1) {
                return 20;
            } else if (deck == 2) {
                return 10;
            } else if (deck == 3) {
                return 4;
            }
        } else if (card instanceof UnityFighter) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 15;
            }
        }

        //Blob
        if (card instanceof BattleBlob) {
            if (deck < 3) {
                return 40;
            }
            return 60;
        } else if (card instanceof BattlePod) {
            if (deck < 3) {
                return 15;
            }
            return 10;
        } else if (card instanceof BattleScreecher) {
            if (deck < 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof Bioformer) {
            if (deck == 1) {
                return 45;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof BlobCarrier) {
            if (deck < 3) {
                return 20;
            }
            return 40;
        } else if (card instanceof BlobDestroyer) {
            if (opponentBases > 3) {
                return 50;
            } else if (opponentBases > 2) {
                return 40;
            } else if (deck < 3) {
                return 30;
            }
            return 40;
        } else if (card instanceof BlobFighter) {
            if (deck <= 2) {
                return 0;
            }
            return 5;
        } else if (card instanceof BlobWheel) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 15;
            }
        } else if (card instanceof BlobWorld) {
            if (bases > 3) {
                return 60;
            }
            return 50;
        } else if (card instanceof CargoPod) {
            if (deck == 1) {
                return 65;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof DeathWorld) {
            if (deck < 3) {
                return 55;
            }
            if (deck == 3) {
                return 45;
            }
            return 40;
        } else if (card instanceof BreedingSite) {
            if (deck < 3 && bases > 0) {
                return 35;
            } else if (deck < 3 || bases >= 2) {
                return 30;
            } else if (bases == 0) {
                return 10;
            }
            return 15;
        } else if (card instanceof Leviathan) {
            return 70;
        } else if (card instanceof Moonwurm) {
            return 55;
        } else if (card instanceof Mothership) {
            return 50;
        } else if (card instanceof Obliterator) {
            if (opponentBases > 3) {
                return 60;
            } else if (opponentBases > 2) {
                return 50;
            }
            return 40;
        } else if (card instanceof Parasite) {
            return 30;
        } else if (card instanceof PlasmaVent) {
            if (player.blobCardPlayedThisTurn()) {
                return 15;
            } else {
                return 5;
            }
        } else if (card instanceof Predator) {
            if (deck <= 2) {
                return 0;
            }
            return 6;
        } else if (card instanceof Ram) {
            if (deck < 3) {
                return 40;
            }
            return 20;
        } else if (card instanceof Ravager) {
            return 20;
        } else if (card instanceof SpikePod) {
            if (deck < 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof StellarReef) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof Swarmer) {
            if (deck < 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof TheHive) {
            if (bases > 3) {
                return 15;
            }
            return 5;
        } else if (card instanceof TradePod) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof TradeWheel) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 10;
            }
        }

        //Trade Federation
        else if (card instanceof BarterWorld) {
            if (deck == 1) {
                return 65;
            } else if (deck == 2) {
                return 50;
            }
            return 40;
        } else if (card instanceof CapitolWorld) {
            if (deck <= 3) {
                return 100;
            }
            return 90;
        } else if (card instanceof CentralOffice) {
            if (deck < 3) {
                return 85;
            } else if (deck == 3) {
                return 60;
            }
            return 50;
        } else if (card instanceof CentralStation) {
            if (deck == 1 && bases > 0) {
                return 70;
            } else if (deck == 1) {
                return 60;
            } else if (deck == 2 && bases > 0) {
                return 55;
            } else if (deck == 2) {
                return 40;
            } else if (deck == 3 && bases >= 2) {
                return 30;
            } else if (deck == 3) {
                return 10;
            }
            if (bases >= 3) {
                return 10;
            }
        } else if (card instanceof ColonySeedShip) {
            if (player.tradeFederationCardPlayedThisTurn()) {
                if (deck == 1) {
                    return 95;
                } else if (deck == 2) {
                    return 85;
                } else if (deck == 3) {
                    return 60;
                }
                return 50;
            } else {
                if (deck == 1) {
                    return 90;
                } else if (deck == 2) {
                    return 80;
                } else if (deck == 3) {
                    return 50;
                }
                return 40;
            }
        } else if (card instanceof CommandShip) {
            return 100;
        } else if (card instanceof ConstructionHauler) {
            if (deck < 3) {
                return 80;
            } else if (deck == 3) {
                return 60;
            }
            return 40;
        } else if (card instanceof CustomsFrigate) {
            if (deck < 3) {
                return 50;
            } else if (deck == 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof Cutter) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 70;
            } else if (deck == 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof DefenseCenter) {
            if (deck == 1) {
                return 10;
            } else if (deck == 2) {
                return 25;
            }
            return 50;
        } else if (card instanceof EmbassyYacht) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2 && bases > 1) {
                return 50;
            } else if (deck == 2) {
                return 30;
            } else if (bases > 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof FactoryWorld) {
            if (deck == 1) {
                return 95;
            } else if (deck == 2) {
                return 85;
            } else if (deck == 3) {
                return 60;
            }
            return 40;
        } else if (card instanceof FederationShipyard) {
            if (deck == 1) {
                return 75;
            } else if (deck == 2) {
                return 65;
            } else if (deck == 3) {
                return 40;
            }
            return 20;
        } else if (card instanceof FederationShuttle) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof Flagship) {
            if (deck < 3) {
                return 50;
            }
            return 70;
        } else if (card instanceof Freighter) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 50;
            } else if (deck == 3) {
                return 20;
            }
        } else if (card instanceof FrontierFerry) {
            if (deck == 1) {
                return 90;
            } else if (deck == 2) {
                return 80;
            } else if (deck == 3) {
                return 50;
            }
            return 40;
        } else if (card instanceof LoyalColony) {
            if (deck < 3) {
                return 85;
            } else if (deck == 3) {
                return 60;
            }
            return 50;
        } else if (card instanceof Megahauler) {
            if (deck < 3) {
                return 90;
            } else if (deck == 3) {
                return 75;
            }
            return 50;
        } else if (card instanceof PortOfCall) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 70;
            }
            return 40;
        } else if (card instanceof PatrolCutter) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 70;
            } else if (deck == 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof Peacekeeper) {
            return 70;
        } else if (card instanceof SolarSkiff) {
            if (deck == 1) {
                return 45;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof Starmarket) {
            if (deck == 1 && bases > 0) {
                return 75;
            } else if (deck == 1) {
                return 65;
            } else if (deck == 2 && bases > 0) {
                return 60;
            } else if (deck == 2) {
                return 50;
            } else if (deck == 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof StorageSilo) {
            if (deck == 1) {
                return 20;
            } else if (deck == 2) {
                return 10;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof TradeEscort) {
            if (deck < 3) {
                return 30;
            }
            return 60;
        } else if (card instanceof TradeHauler) {
            if (deck == 1) {
                return 55;
            } else if (deck == 2) {
                return 35;
            } else if (deck == 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof TradeRaft) {
            if (deck == 1) {
                return 45;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof TradingPost) {
            if (deck == 1) {
                return 25;
            } else if (deck == 2) {
                return 10;
            }
            return 5;
        }

        //Star Empire
        else if (card instanceof AgingBattleship) {
            return 60;
        } else if (card instanceof BattleBarge) {
            if (bases >= 4) {
                return 50;
            } else if (bases >= 2) {
                return 40;
            }
            return 30;
        } else if (card instanceof Battlecruiser) {
            return 70;
        } else if (card instanceof CargoLaunch) {
            return 5;
        } else if (card instanceof CommandCenter) {
            int numberOfStarEmpireCards = player.countCardsByType(player.getAllCards(), c -> c.hasFaction(Faction.STAR_EMPIRE));
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 30 + (5 * numberOfStarEmpireCards);
            }
            return 10 + (5 * numberOfStarEmpireCards);
        } else if (card instanceof Corvette) {
            if (deck < 3) {
                return 5;
            }
            return 10;
        } else if (card instanceof Dreadnaught) {
            return 80;
        } else if (card instanceof EmperorsDreadnaught) {
            if (player.starEmpireCardPlayedThisTurn()) {
                return 105;
            } else {
                return 85;
            }
        } else if (card instanceof Falcon) {
            if (deck < 3) {
                return 5;
            }
            return 10;
        } else if (card instanceof FighterBase) {
            if (deck == 1) {
                return 10;
            }
            return 20;
        } else if (card instanceof FleetHQ) {
            if (deck < 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof Gunship) {
            return 10;
        } else if (card instanceof HeavyCruiser) {
            return 60;
        } else if (card instanceof ImperialFighter) {
            if (deck <= 2) {
                return 0;
            }
            return 5;
        } else if (card instanceof ImperialFrigate) {
            if (deck < 3) {
                return 10;
            }
            return 30;
        } else if (card instanceof ImperialPalace) {
            if (player.countCardsByType(player.getAllCards(), c -> c.hasFaction(Faction.STAR_EMPIRE)) > 2) {
                return 50;
            }
            return 35;
        } else if (card instanceof ImperialTrader) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 40;
            } else if (deck == 3) {
                return 30;
            }
            return 20;
        } else if (card instanceof Lancer) {
            if (deck < 3) {
                if (opponentBases >= 1) {
                    return 5;
                }
                return 0;
            }
            if (opponentBases >= 2) {
                return 10;
            } else {
                return 5;
            }
        } else if (card instanceof OrbitalPlatform) {
            if (deck == 1) {
                return 0;
            }
            if (deck < 3) {
                return 5;
            }
            return 10;
        } else if (card instanceof RecyclingStation) {
            if (deck == 1) {
                return 30;
            }
            if (player.countCardsByType(player.getAllCards(), Card::isStarterCard) >= 4) {
                return 45;
            }
            return 30;
        } else if (card instanceof RoyalRedoubt) {
            if (bases > 2) {
                return 40;
            }
            return 20;
        } else if (card instanceof SpaceStation) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 40;
            }
            return 25;
        } else if (card instanceof StarBarge) {
            if (deck < 3) {
                return 10;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof StarFortress) {
            if (deck <= 3) {
                return 50;
            }
            return 40;
        } else if (card instanceof StarbaseOmega) {
            if (deck < 3 && bases > 0) {
                return 40;
            } else if (deck == 3 && bases >= 2) {
                return 30;
            } else if (deck < 3) {
                return 20;
            } else if (bases == 0) {
                return 5;
            }
            return 10;
        } else if (card instanceof SurveyShip) {
            return 5;
        } else if (card instanceof SupplyDepot) {
            if (deck < 3) {
                return 50;
            } else if (deck == 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof WarWorld) {
            if (bases > 2) {
                return 30;
            }
            return 10;
        }

        //Machine Cult
        else if (card instanceof BattleBot) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 10;
            }
            if (player.countCardsByType(player.getAllCards(), Card::isStarterCard) >= 2) {
                return 5;
            }
        } else if (card instanceof BattleMech) {
            if (deck < 3) {
                return 45;
            }
            return 40;
        } else if (card instanceof BattleStation) {
            if (deck == 1) {
                return 10;
            } else if (deck == 2) {
                return 20;
            }
            return 30;
        } else if (card instanceof BorderFort) {
            if (deck == 1) {
                return 65;
            } else if (deck == 2) {
                return 45;
            } else if (deck == 3) {
                return 35;
            }
            return 30;
        } else if (card instanceof BrainWorld) {
            if (deck < 3) {
                return 100;
            }
            if (deck == 3) {
                return 80;
            }
            return 70;
        } else if (card instanceof ConvoyBot) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 50;
            } else if (deck == 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof DefenseBot) {
            if (deck < 3 && bases > 0) {
                return 40;
            } else if (deck < 3) {
                return 35;
            } else if (deck == 3 && bases >= 2) {
                return 45;
            } else if (deck == 3 && bases > 0) {
                return 30;
            } else if (bases >= 2) {
                return 35;
            }
            return 10;
        } else if (card instanceof FortressOblivion) {
            if (deck < 3 && bases > 0) {
                return 40;
            } else if (deck < 3) {
                return 35;
            } else if (deck == 3 && bases >= 2) {
                return 30;
            } else if (deck ==3 && bases > 0) {
                return 20;
            }
            return 2;
        } else if (card instanceof FrontierStation) {
            if (deck < 3) {
                return 55;
            } else if (deck == 3) {
                return 45;
            }
            return 30;
        } else if (card instanceof Junkyard) {
            if (deck < 3) {
                return 40;
            } else if (bases > 3) {
                return 25;
            }
            return 5;
        } else if (card instanceof MachineBase) {
            return 70;
        } else if (card instanceof MechCruiser) {
            if (deck < 3) {
                return 45;
            }
            return 40;
        } else if (card instanceof MechWorld) {
            if (deck == 1) {
                return 15;
            }
            return 40;
        } else if (card instanceof MegaMech) {
            if (bases >= 4) {
                return 65;
            } else if (bases >= 2) {
                return 55;
            }
            return 45;
        } else if (card instanceof MiningMech) {
            if (deck == 1) {
                return 75;
            } else if (deck == 2) {
                return 55;
            } else if (deck == 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof MissileBot) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 40;
            } else if (deck == 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof MissileMech) {
            if (opponentBases > 3) {
                return 70;
            }
            return 50;
        } else if (card instanceof PatrolBot) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 30;
            }
            return 15;
        } else if (card instanceof PatrolMech) {
            if (deck == 1) {
                return 70;
            } else if (deck == 2) {
                return 50;
            }
            return 30;
        } else if (card instanceof RepairBot) {
            if (deck == 1) {
                return 55;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 10;
            }
        } else if (card instanceof SupplyBot) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 15;
            }
            if (player.countCardsByType(player.getAllCards(), Card::isStarterCard) >= 2) {
                return 5;
            }
        } else if (card instanceof StealthNeedle) {
            if (deck < 3) {
                return 30;
            }
            return 50;
        } else if (card instanceof StealthTower) {
            int totalBases = bases + opponentBases;
            if (deck < 3) {
                return 30 + (5 * totalBases);
            } else {
                return 10 + (5 * totalBases);
            }
        } else if (card instanceof TheArk) {
            if (deck < 3) {
                return 100;
            }
            return 90;
        } else if (card instanceof TheIncinerator) {
            if (deck < 3) {
                return 90;
            }
            if (deck == 3) {
                return 70;
            }
            return 60;
        } else if (card instanceof TheOracle) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 45;
            } else if (deck == 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof TheWrecker) {
            if (deck < 3) {
                return 90;
            } else if (deck == 3) {
                return 80;
            }
            return 70;
        } else if (card instanceof TradeBot) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof WarningBeacon) {
            if (player.machineCultCardPlayedThisTurn()) {
                return 20;
            } else {
                return 5;
            }
        }

        //Other
        else if (card instanceof Explorer) {
            if (deck == 1) {
                return 15;
            } else if (deck == 2) {
                return 5;
            } else if (deck == 3) {
                return 2;
            }
        }
        else if (card instanceof MercCruiser) {
            if (deck == 1) {
                return 5;
            } else if (deck == 2) {
                return 10;
            }
            return 20;
        }

        return 0;
    }
}
