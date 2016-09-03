package org.smartreaction.starrealms.model.players.bots.strategies;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.bases.blob.BlobWorld;
import org.smartreaction.starrealms.model.cards.bases.blob.DeathWorld;
import org.smartreaction.starrealms.model.cards.bases.outposts.machinecult.*;
import org.smartreaction.starrealms.model.cards.bases.outposts.starempire.RoyalRedoubt;
import org.smartreaction.starrealms.model.cards.bases.outposts.starempire.SpaceStation;
import org.smartreaction.starrealms.model.cards.bases.outposts.starempire.StarFortress;
import org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation.DefenseCenter;
import org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation.PortOfCall;
import org.smartreaction.starrealms.model.cards.bases.outposts.tradefederation.TradingPost;
import org.smartreaction.starrealms.model.cards.bases.outposts.united.AllianceLanding;
import org.smartreaction.starrealms.model.cards.bases.outposts.united.CoalitionFortress;
import org.smartreaction.starrealms.model.cards.bases.outposts.united.LookoutPost;
import org.smartreaction.starrealms.model.cards.bases.outposts.united.UnityStation;
import org.smartreaction.starrealms.model.cards.bases.tradefederation.BarterWorld;
import org.smartreaction.starrealms.model.cards.bases.tradefederation.CentralOffice;
import org.smartreaction.starrealms.model.cards.bases.tradefederation.Starmarket;
import org.smartreaction.starrealms.model.cards.bases.united.EmbassyBase;
import org.smartreaction.starrealms.model.cards.bases.united.ExchangePoint;
import org.smartreaction.starrealms.model.cards.bases.united.UnionCluster;
import org.smartreaction.starrealms.model.cards.bases.united.UnionStronghold;
import org.smartreaction.starrealms.model.cards.heroes.*;
import org.smartreaction.starrealms.model.cards.heroes.united.*;
import org.smartreaction.starrealms.model.cards.ships.blob.BlobFighter;
import org.smartreaction.starrealms.model.cards.ships.machinecult.*;
import org.smartreaction.starrealms.model.cards.ships.starempire.Battlecruiser;
import org.smartreaction.starrealms.model.cards.ships.starempire.CargoLaunch;
import org.smartreaction.starrealms.model.cards.ships.starempire.Dreadnaught;
import org.smartreaction.starrealms.model.cards.ships.tradefederation.*;
import org.smartreaction.starrealms.model.cards.ships.united.*;
import org.smartreaction.starrealms.model.players.Player;

public class AttackVelocityStrategy implements BotStrategy {
    AttackStrategy attackStrategy = new AttackStrategy();

    @Override
    public int getBuyCardScore(Card card, Player player) {
        int deck = player.getCurrentDeckNumber();
        int bases = player.getNumBasesInAllCards();

        //Heroes
        if (card instanceof RamPilot) {
            if (deck == 1) {
                return 15;
            } else if (deck <= 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof BlobOverlord) {
            if (deck == 1) {
                return 20;
            } else if (deck <= 3) {
                return 40;
            }
            return 20;
        } else if (card instanceof SpecialOpsDirector) {
            if (deck == 1) {
                return 0;
            } else if (deck <= 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof CeoTorres) {
            if (deck == 1) {
                return 5;
            } else if (deck <= 3) {
                return 15;
            }
            return 10;
        } else if (card instanceof WarElder) {
            if (deck == 1) {
                return 20;
            } else if (deck <= 3) {
                return 35;
            }
            return 10;
        } else if (card instanceof HighPriestLyle) {
            if (deck == 1) {
                return 30;
            } else if (deck <= 3) {
                return 45;
            }
            return 15;
        } else if (card instanceof CunningCaptain) {
            if (deck == 1) {
                return 15;
            } else if (deck <= 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof AdmiralRasmussen) {
            if (deck == 1) {
                return 20;
            } else if (deck <= 3) {
                return 40;
            }
            return 20;
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
                return 30;
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
                return 25;
            } else if (deck <= 3) {
                return 45;
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
            }
        } else if (card instanceof AssaultPod) {
            if (deck == 1) {
                return 15;
            } else if (deck == 2) {
                return 30;
            }
            return 50;
        } else if (card instanceof BlobBot) {
            if (deck <= 2) {
                return 40;
            } else if (deck == 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof CoalitionFreighter) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof CoalitionMessenger) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 15;
            }
        } else if (card instanceof TradeStar) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 10;
            }
        } else if (card instanceof UnityFighter) {
            if (deck == 1) {
                return 70;
            } else if (deck == 2) {
                return 50;
            } else if (deck == 3) {
                return 30;
            }
            return 10;
        }

        //United Bases
        if (card instanceof EmbassyBase) {
            return 75;
        } else if (card instanceof ExchangePoint) {
            if (deck < 3) {
                return 70;
            }
            if (deck == 3) {
                return 60;
            }
            return 35;
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
            }
        } else if (card instanceof CoalitionFortress) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 10;
            }
        } else if (card instanceof LookoutPost) {
            if (deck < 3) {
                return 45;
            }
            if (deck == 3) {
                return 35;
            }
            return 20;
        } else if (card instanceof UnityStation) {
            if (deck < 3) {
                return 75;
            } else if (bases > 3) {
                return 55;
            } else if (deck == 3) {
                return 35;
            }
            return 20;
        }

        //Blob
        if (card instanceof BlobFighter) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 55;
            } else {
                return 65;
            }
        } else if (card instanceof BlobWorld) {
            return 100;
        } else if (card instanceof DeathWorld) {
            if (deck < 3) {
                return 95;
            }
            if (deck == 3) {
                return 85;
            }
            return 75;
        }

        //Trade Federation
        else if (card instanceof BarterWorld) {
            if (deck == 1) {
                return 30;
            }
            return 15;
        } else if (card instanceof CentralOffice) {
            if (deck < 3) {
                return 35;
            } else if (deck == 3) {
                return 20;
            }
            return 15;
        } else if (card instanceof CommandShip) {
            return 80;
        } else if (card instanceof Cutter) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 40;
            }
            return 10;
        } else if (card instanceof DefenseCenter) {
            return 10;
        } else if (card instanceof EmbassyYacht) {
            if (deck == 1) {
                return 15;
            } else if (bases > 3) {
                return 30;
            }
        } else if (card instanceof FederationShuttle) {
            if (deck == 1) {
                return 15;
            }
        } else if (card instanceof Flagship) {
            return 60;
        } else if (card instanceof Freighter) {
            if (deck == 1) {
                return 30;
            }
        } else if (card instanceof PortOfCall) {
            if (deck == 1) {
                return 30;
            }
            return 15;
        } else if (card instanceof Starmarket) {
            if (deck == 1 && bases > 0) {
                return 10;
            } else if (deck == 1) {
                return 5;
            } else if (deck == 2 && bases > 0) {
                return 5;
            }
            return 0;
        } else if (card instanceof TradeEscort) {
            return 15;
        } else if (card instanceof TradeRaft) {
            if (deck == 1) {
                return 15;
            }
        } else if (card instanceof TradingPost) {
            if (deck == 1) {
                return 20;
            }
            return 5;
        }

        //Star Empire
        else if (card instanceof Battlecruiser) {
            return 70;
        } else if (card instanceof CargoLaunch) {
            return 10;
        } else if (card instanceof Dreadnaught) {
            return 90;
        } else if (card instanceof RoyalRedoubt) {
            return 40;
        } else if (card instanceof SpaceStation) {
            if (deck == 1) {
                return 40;
            }
            return 30;
        } else if (card instanceof StarFortress) {
            if (deck <= 3) {
                return 90;
            }
            return 80;
        }

        //Machine Cult
        else if (card instanceof BattleBot) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 15;
            }
        } else if (card instanceof BattleMech) {
            if (deck < 3) {
                return 50;
            }
            return 40;
        } else if (card instanceof BattleStation) {
            if (deck < 3) {
                return 15;
            }
            return 30;
        } else if (card instanceof BorderFort) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 15;
            }
        } else if (card instanceof BrainWorld) {
            if (deck < 3) {
                return 85;
            }
            if (deck == 3) {
                return 50;
            }
            return 20;
        } else if (card instanceof ConvoyBot) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof DefenseBot) {
            if (deck < 3 && bases > 0) {
                return 20;
            } else if (deck < 3) {
                return 10;
            }
        } else if (card instanceof FortressOblivion) {
            if (deck < 3 && bases > 0) {
                return 20;
            } else if (deck < 3) {
                return 10;
            }
        } else if (card instanceof Junkyard) {
            if (deck < 3) {
                return 10;
            }
        } else if (card instanceof MachineBase) {
            if (deck < 3) {
                return 50;
            }
            if (deck == 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof MechCruiser) {
            if (deck < 3) {
                return 60;
            }
            return 50;
        } else if (card instanceof MechWorld) {
            return 15;
        } else if (card instanceof MiningMech) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 20;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof MissileBot) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 30;
            }
            return 5;
        } else if (card instanceof PatrolBot) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 20;
            }
            return 10;
        } else if (card instanceof PatrolMech) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 40;
            }
            return 20;
        } else if (card instanceof RepairBot) {
            if (deck == 1) {
                return 25;
            } else if (deck == 2) {
                return 15;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof SupplyBot) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof TheArk) {
            if (deck < 3) {
                return 90;
            }
            return 80;
        } else if (card instanceof TheIncinerator) {
            if (deck < 3) {
                return 80;
            }
            if (deck == 3) {
                return 45;
            }
            return 20;
        } else if (card instanceof TheOracle) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 15;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof TheWrecker) {
            if (deck < 3) {
                return 80;
            } else if (deck == 3) {
                return 70;
            }
            return 60;
        } else if (card instanceof TradeBot) {
            if (deck == 1) {
                return 20;
            } else if (deck == 2) {
                return 10;
            }
            return 0;
        }

        return attackStrategy.getBuyCardScore(card, player);
    }
}
