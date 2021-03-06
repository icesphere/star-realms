package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.blob.*;
import org.smartreaction.starrealms.model.cards.bases.outposts.machinecult.*;
import org.smartreaction.starrealms.model.cards.bases.outposts.starempire.*;
import org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation.*;
import org.smartreaction.starrealms.model.cards.bases.outposts.united.AllianceLanding;
import org.smartreaction.starrealms.model.cards.bases.outposts.united.CoalitionFortress;
import org.smartreaction.starrealms.model.cards.bases.outposts.united.LookoutPost;
import org.smartreaction.starrealms.model.cards.bases.outposts.united.UnityStation;
import org.smartreaction.starrealms.model.cards.bases.starempire.FleetHQ;
import org.smartreaction.starrealms.model.cards.bases.starempire.OrbitalPlatform;
import org.smartreaction.starrealms.model.cards.bases.starempire.StarbaseOmega;
import org.smartreaction.starrealms.model.cards.bases.tradefederation.*;
import org.smartreaction.starrealms.model.cards.bases.united.EmbassyBase;
import org.smartreaction.starrealms.model.cards.bases.united.ExchangePoint;
import org.smartreaction.starrealms.model.cards.bases.united.UnionCluster;
import org.smartreaction.starrealms.model.cards.bases.united.UnionStronghold;
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

public class AttackStrategy implements BotStrategy {
    @Override
    public int getBuyCardScore(Card card, Player player) {
        int deck = player.getCurrentDeckNumber();
        int bases = player.getNumBasesInAllCards();
        Player opponent = player.getOpponent();
        int opponentBases = opponent.getNumBasesInAllCards();
        int numStarEmpireCards = player.countCardsByType(player.getAllCards(), c -> c.hasFaction(Faction.STAR_EMPIRE));

        //Heroes
        if (card instanceof RamPilot) {
            if (deck == 1) {
                return 20;
            } else if (deck <= 3) {
                return 25;
            }
            return 15;
        } else if (card instanceof BlobOverlord) {
            if (deck == 1) {
                return 21;
            } else if (deck <= 3) {
                return 30;
            }
            return 20;
        } else if (card instanceof SpecialOpsDirector) {
            if (deck == 1) {
                return 5;
            } else if (deck <= 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof CeoTorres) {
            if (deck == 1) {
                return 7;
            } else if (deck <= 3) {
                return 15;
            }
            return 10;
        } else if (card instanceof WarElder) {
            if (deck == 1) {
                return 10;
            } else if (deck <= 3) {
                return 20;
            }
            return 5;
        } else if (card instanceof HighPriestLyle) {
            if (deck == 1) {
                return 15;
            } else if (deck <= 3) {
                return 25;
            }
            return 7;
        } else if (card instanceof CunningCaptain) {
            if (deck == 1) {
                return 10;
            }

            return 20;
        } else if (card instanceof AdmiralRasmussen) {
            if (deck == 1) {
                return 20;
            } else if (deck <= 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof CEOShaner) {
            return 20;
        } else if (card instanceof ChairmanHaygan) {
            if (deck == 1) {
                return 5;
            } else if (deck <= 3) {
                return 15;
            }
            return 10;
        } else if (card instanceof ChancellorHartman) {
            if (deck == 1) {
                return 25;
            } else if (deck <= 3) {
                return 35;
            }
            return 15;
        } else if (card instanceof ConfessorMorris) {
            if (deck == 1) {
                return 35;
            } else if (deck <= 3) {
                return 45;
            }
            return 25;
        } else if (card instanceof CommanderKlik) {
            if (deck == 1) {
                return 25;
            } else if (deck <= 3) {
                return 50;
            }
            return 30;
        } else if (card instanceof CommanderZhang) {
            if (deck == 1) {
                return 35;
            } else if (deck <= 3) {
                return 55;
            }
            return 60;
        } else if (card instanceof Screecher) {
            if (deck == 1) {
                return 20;
            } else if (deck <= 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof HiveLord) {
            if (deck == 1) {
                return 35;
            } else if (deck <= 3) {
                return 55;
            }
            return 60;
        }

        //United
        if (card instanceof AllianceFrigate) {
            if (deck < 3) {
                return 25;
            }
            return 35;
        } else if (card instanceof AllianceTransport) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 5;
            } else if (deck == 4) {
                return 1;
            }
        } else if (card instanceof AssaultPod) {
            if (deck == 1) {
                return 25;
            } else if (deck == 2) {
                return 40;
            }
            return 60;
        } else if (card instanceof BlobBot) {
            if (deck <= 2) {
                return 40;
            } else if (deck == 3) {
                return 30;
            }
            return 6;
        } else if (card instanceof CoalitionFreighter) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 15;
            }
        } else if (card instanceof CoalitionMessenger) {
            if (deck == 1) {
                return 25;
            } else if (deck == 2) {
                return 10;
            }
        } else if (card instanceof TradeStar) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 15;
            } else if (deck == 4) {
                return 6;
            }
            return 1;
        } else if (card instanceof UnityFighter) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 40;
            } else if (deck == 3) {
                return 20;
            }
            return 10;
        }

        //United Bases
        if (card instanceof EmbassyBase) {
            return 70;
        } else if (card instanceof ExchangePoint) {
            if (deck < 3) {
                return 65;
            }
            if (deck == 3) {
                return 55;
            }
            return 30;
        } else if (card instanceof UnionCluster) {
            return 95;
        } else if (card instanceof UnionStronghold) {
            return 35;
        } else if (card instanceof AllianceLanding) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 5;
            } else if (deck == 4) {
                return 1;
            }
        } else if (card instanceof CoalitionFortress) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof LookoutPost) {
            if (deck < 3) {
                return 30;
            }
            if (deck == 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof UnityStation) {
            if (deck < 3) {
                return 65;
            } else if (bases > 3) {
                return 45;
            } else if (deck == 3) {
                return 25;
            }
            return 15;
        }

        //Blob
        if (card instanceof BattleBlob) {
            return 90;
        } else if (card instanceof BattlePod) {
            if (deck < 3) {
                return 45;
            } else if (deck == 3) {
                return 30;
            }
            return 25;
        } else if (card instanceof BattleScreecher) {
            if (deck < 3) {
                return 50;
            } else if (deck == 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof Bioformer) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 35;
            } else if (deck == 3) {
                return 10;
            } else if (deck == 4) {
                return 4;
            }
        } else if (card instanceof BlobCarrier) {
            if (deck < 3 || (player.getGame().getCardSets().contains(CardSet.CRISIS_HEROES) || player.getGame().getCardSets().contains(CardSet.UNITED_HEROES))) {
                return 90;
            }
            return 70;
        } else if (card instanceof BlobDestroyer) {
            if (opponentBases > 3) {
                return 80;
            } else if (opponentBases > 2) {
                return 70;
            }
            return 60;
        } else if (card instanceof BlobFighter) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 50;
            } else {
                return 60;
            }
        } else if (card instanceof BlobWheel) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof BlobWorld) {
            return 80;
        } else if (card instanceof BreedingSite) {
            if (deck < 3 && bases > 0) {
                return 55;
            } else if (deck < 3 || bases >= 2) {
                return 50;
            } else if (bases == 0) {
                return 35;
            }
            return 45;
        } else if (card instanceof CargoPod) {
            if (deck == 1) {
                return 85;
            } else if (deck == 2) {
                return 45;
            } else if (deck == 3) {
                return 15;
            } else {
                return 10;
            }
        } else if (card instanceof DeathWorld) {
            return 75;
        } else if (card instanceof Leviathan) {
            return 120;
        } else if (card instanceof Moonwurm) {
            return 100;
        } else if (card instanceof Mothership) {
            return 100;
        } else if (card instanceof Obliterator) {
            if (opponentBases > 3) {
                return 95;
            } else if (opponentBases > 2) {
                return 85;
            }
            return 80;
        } else if (card instanceof Parasite) {
            if (deck < 3) {
                return 75;
            }
            return 60;
        } else if (card instanceof PlasmaVent) {
            if (player.blobCardPlayedThisTurn()) {
                return 70;
            } else {
                if (deck <= 2) {
                    return 20;
                }
                return 50;
            }
        } else if (card instanceof Predator) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 60;
            } else {
                return 70;
            }
        } else if (card instanceof Ram) {
            if (deck < 3) {
                return 70;
            }
            return 55;
        } else if (card instanceof Ravager) {
            return 45;
        } else if (card instanceof SpikePod) {
            if (deck < 3) {
                return 35;
            }
            return 25;
        } else if (card instanceof StellarReef) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof Swarmer) {
            if (deck < 3) {
                return 35;
            }
            return 25;
        } else if (card instanceof TheHive) {
            if (deck == 1) {
                return 25;
            } else if (deck == 2) {
                return 35;
            }
            return 50;
        } else if (card instanceof TradePod) {
            if (deck == 1) {
                return 75;
            } else if (deck == 2) {
                return 40;
            } else if (deck == 3) {
                return 9;
            } else if (deck == 4) {
                return 3;
            }
        } else if (card instanceof TradeWheel) {
            if (deck == 1) {
                return 39;
            } else if (deck == 2) {
                return 29;
            } else if (deck == 3) {
                return 6;
            }
        }

        //Trade Federation
        else if (card instanceof BarterWorld) {
            if (deck == 1) {
                return 25;
            }
            return 10;
        } else if (card instanceof CapitolWorld) {
            if (deck <= 3) {
                return 55;
            }
            return 30;
        } else if (card instanceof CentralOffice) {
            return 20;
        } else if (card instanceof CentralStation) {
            if (deck == 1) {
                return 5;
            }
        } else if (card instanceof ColonySeedShip) {
            if (player.tradeFederationCardPlayedThisTurn()) {
                if (deck == 1) {
                    return 80;
                } else if (deck == 2) {
                    return 50;
                } else if (deck == 3) {
                    return 40;
                }
                return 30;
            } else {
                if (deck == 1) {
                    return 70;
                } else if (deck == 2) {
                    return 30;
                } else if (deck == 3) {
                    return 20;
                }
                return 10;
            }
        } else if (card instanceof CommandShip) {
            return 75;
        } else if (card instanceof ConstructionHauler) {
            if (deck < 3) {
                return 30;
            } else if (deck == 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof CustomsFrigate) {
            if (deck < 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof Cutter) {
            if (deck == 1) {
                return 65;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof DefenseCenter) {
            return 5;
        } else if (card instanceof EmbassyYacht) {
            return 0;
        } else if (card instanceof FactoryWorld) {
            if (deck <= 2) {
                return 20;
            }
            return 10;
        } else if (card instanceof FederationShipyard) {
            if (deck <= 2) {
                return 10;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof FederationShuttle) {
            return 0;
        } else if (card instanceof Flagship) {
            return 50;
        } else if (card instanceof Freighter) {
            if (deck == 1) {
                return 10;
            }
            return 0;
        } else if (card instanceof FrontierFerry) {
            if (deck == 1) {
                return 65;
            } else if (deck == 2) {
                return 25;
            } else {
                return 20;
            }
        } else if (card instanceof LoyalColony) {
            if (deck <= 2) {
                return 11;
            }
            return 6;
        } else if (card instanceof Megahauler) {
            if (deck < 3) {
                return 50;
            } else if (deck == 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof PortOfCall) {
            if (deck <= 2) {
                return 15;
            }
            return 10;
        } else if (card instanceof PatrolCutter) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 4;
            }
        } else if (card instanceof Peacekeeper) {
            return 50;
        } else if (card instanceof SolarSkiff) {
            return 0;
        } else if (card instanceof Starmarket) {
            if (deck == 1) {
                return 10;
            }
        } else if (card instanceof StorageSilo) {
            return 0;
        } else if (card instanceof TradeEscort) {
            if (deck <= 2) {
                return 20;
            }
            return 10;
        } else if (card instanceof TradeHauler) {
            if (deck == 1) {
                return 5;
            }
        } else if (card instanceof TradeRaft) {
            return 0;
        } else if (card instanceof TradingPost) {
            if (deck == 1) {
                return 10;
            }
        }

        //Star Empire
        else if (card instanceof AgingBattleship) {
            return 69;
        } else if (card instanceof BattleBarge) {
            if (bases >= 4) {
                return 65;
            } else if (bases >= 2) {
                return 55;
            }
            return 40;
        } else if (card instanceof Battlecruiser) {
            return 75;
        } else if (card instanceof CargoLaunch) {
            return 10;
        } else if (card instanceof CommandCenter) {
            int numberOfStarEmpireCards = player.countCardsByType(player.getAllCards(), c -> c.hasFaction(Faction.STAR_EMPIRE));
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 15 + (5 * numberOfStarEmpireCards);
            }
            return 5 * numberOfStarEmpireCards;
        } else if (card instanceof Corvette) {
            return 20;
        } else if (card instanceof Dreadnaught) {
            return 95;
        } else if (card instanceof EmperorsDreadnaught) {
            if (player.starEmpireCardPlayedThisTurn()) {
                return 120;
            } else {
                return 94;
            }
        } else if (card instanceof Falcon) {
            if (deck < 3) {
                return 15;
            }
            return 20;
        } else if (card instanceof FighterBase) {
            if (bases >= 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof FleetHQ) {
            if (deck < 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof Gunship) {
            if (deck <= 2) {
                return 50;
            }
            return 30;
        } else if (card instanceof HeavyCruiser) {
            return 65;
        } else if (card instanceof ImperialFighter) {
            if (deck == 1) {
                return 10;
            }
            return 15;
        } else if (card instanceof ImperialFrigate) {
            return 40;
        } else if (card instanceof ImperialPalace) {
            if (deck <= 2) {
                return 40;
            }
            return 30;
        } else if (card instanceof ImperialTrader) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 65;
            } else if (deck == 3) {
                return 45;
            }
            return 35;
        } else if (card instanceof Lancer) {
            if (deck < 3) {
                if (opponentBases >= 1) {
                    return 15;
                }
                return 10;
            }
            if (opponentBases >= 2) {
                return 20;
            } else {
                return 10;
            }
        } else if (card instanceof OrbitalPlatform) {
            return 5;
        } else if (card instanceof RecyclingStation) {
            if (deck == 1) {
                return 30;
            }
            return 50;
        } else if (card instanceof RoyalRedoubt) {
            if (deck <= 2) {
                return 40;
            }
            return 30;
        } else if (card instanceof SpaceStation) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 20;
            }
            return 10;
        } else if (card instanceof StarBarge) {
            if (deck == 1) {
                return 25;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 10;
            }
            return 3;
        } else if (card instanceof StarFortress) {
            if (deck <= 3) {
                return 85;
            }
            return 70;
        } else if (card instanceof StarbaseOmega) {
            if (deck < 3 && bases > 0) {
                return 5;
            } else if (deck < 3) {
                return 0;
            } else if (bases >= 2) {
                return 5;
            } else if (bases == 0) {
                return 0;
            }
            return 5;
        } else if (card instanceof SurveyShip) {
            return 5;
        } else if (card instanceof SupplyDepot) {
            if (deck < 3) {
                return 40;
            } else if (deck == 3) {
                return 25;
            }
            return 20;
        } else if (card instanceof WarWorld) {
            if (numStarEmpireCards >= 3) {
                return 50;
            }
            return 30;
        }

        //Machine Cult
        else if (card instanceof BattleBot) {
            if (deck == 1) {
                return 15;
            } else if (deck == 2) {
                return 5;
            } else if (deck == 3) {
                return 1;
            }
        } else if (card instanceof BattleMech) {
            if (deck < 3) {
                return 40;
            }
            return 25;
        } else if (card instanceof BattleStation) {
            if (deck == 1) {
                return 5;
            } else if (deck == 2) {
                return 10;
            }
            return 20;
        } else if (card instanceof BorderFort) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 15;
            }
        } else if (card instanceof BrainWorld) {
            if (deck < 3) {
                return 80;
            }
            if (deck == 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof ConvoyBot) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof DefenseBot) {
            if (deck < 3 && bases > 0) {
                return 10;
            } else if (deck < 3) {
                return 5;
            }
        } else if (card instanceof FortressOblivion) {
            if (deck < 3 && bases > 0) {
                return 9;
            } else if (deck < 3) {
                return 4;
            }
        } else if (card instanceof FrontierStation) {
            if (deck < 3) {
                return 30;
            } else if (deck == 3) {
                return 15;
            }
            return 5;
        } else if (card instanceof Junkyard) {
            return 0;
        } else if (card instanceof MachineBase) {
            if (deck < 3) {
                return 25;
            }
            if (deck == 3) {
                return 10;
            }
        } else if (card instanceof MechCruiser) {
            if (deck < 3) {
                return 50;
            }
            return 35;
        } else if (card instanceof MechWorld) {
            return 5;
        } else if (card instanceof MegaMech) {
            if (bases >= 4) {
                return 60;
            } else if (bases >= 2) {
                return 50;
            }
            return 35;
        } else if (card instanceof MiningMech) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 10;
            }
        } else if (card instanceof MissileBot) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 15;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof MissileMech) {
            if (opponentBases >= 3) {
                return 75;
            }
            return 60;
        } else if (card instanceof PatrolBot) {
            if (deck == 1) {
                return 25;
            } else if (deck == 2) {
                return 9;
            }
        } else if (card instanceof PatrolMech) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 15;
            } else if (deck == 3) {
                return 4;
            }
        } else if (card instanceof RepairBot) {
            if (deck == 1) {
                return 15;
            } else if (deck == 2) {
                return 5;
            }
            return 3;
        } else if (card instanceof SupplyBot) {
            if (deck == 1) {
                return 20;
            } else if (deck == 2) {
                return 10;
            }
        } else if (card instanceof StealthNeedle) {
            if (deck == 1) {
                return 20;
            } else if (deck == 2) {
                return 40;
            }
            return 60;
        } else if (card instanceof StealthTower) {
            int totalBases = bases + opponentBases;
            if (deck < 3) {
                return 7 * totalBases;
            } else {
                return 4 * totalBases;
            }
        } else if (card instanceof TheArk) {
            if (deck < 3) {
                return 85;
            }
            return 75;
        } else if (card instanceof TheIncinerator) {
            if (deck < 3) {
                return 75;
            }
            if (deck == 3) {
                return 25;
            }
            return 10;
        } else if (card instanceof TheOracle) {
            if (deck == 1) {
                return 15;
            } else if (deck == 2) {
                return 5;
            }
        } else if (card instanceof TheWrecker) {
            if (deck < 3) {
                return 70;
            } else if (deck == 3) {
                return 60;
            }
            return 40;
        } else if (card instanceof TradeBot) {
            if (deck == 1) {
                return 10;
            }
        } else if (card instanceof WarningBeacon) {
            if (player.machineCultCardPlayedThisTurn()) {
                if (deck < 3) {
                    return 10;
                }
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
            } else if (deck == 4) {
                return 1;
            }
        } else if (card instanceof MercCruiser) {
            if (deck == 1) {
                return 20;
            }
            return 40;
        }

        return 0;

    }

    public int getBuyScoreIncreaseForAverageTradeRowCost(int averageTradeRowCost, Card card, int deckNumber) {
        if (averageTradeRowCost >= 5 && deckNumber <= 2) {
            if (card.getTradeWhenPlayed() >= 2 || card.getTradeWhenScrapped() >= 3) {
                if (averageTradeRowCost >= 7) {
                    return 10;
                } else if (averageTradeRowCost >= 6) {
                    return 5;
                }
            }
        }

        return 0;
    }
}
