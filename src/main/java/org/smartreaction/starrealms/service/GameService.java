package org.smartreaction.starrealms.service;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.smartreaction.starrealms.model.CardSet;
import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.GameOptions;
import org.smartreaction.starrealms.model.User;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.bases.Base;
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
import org.smartreaction.starrealms.model.cards.events.*;
import org.smartreaction.starrealms.model.cards.gambits.*;
import org.smartreaction.starrealms.model.cards.heroes.*;
import org.smartreaction.starrealms.model.cards.heroes.united.*;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.cards.ships.MercCruiser;
import org.smartreaction.starrealms.model.cards.ships.Scout;
import org.smartreaction.starrealms.model.cards.ships.Viper;
import org.smartreaction.starrealms.model.cards.ships.blob.*;
import org.smartreaction.starrealms.model.cards.ships.machinecult.*;
import org.smartreaction.starrealms.model.cards.ships.starempire.*;
import org.smartreaction.starrealms.model.cards.ships.tradefederation.*;
import org.smartreaction.starrealms.model.cards.ships.united.*;
import org.smartreaction.starrealms.model.players.HumanPlayer;
import org.smartreaction.starrealms.model.players.Player;
import org.smartreaction.starrealms.model.players.bots.SimulatorBot;
import org.smartreaction.starrealms.model.players.bots.StrategyBot;
import org.smartreaction.starrealms.model.players.bots.strategies.*;
import org.smartreaction.starrealms.model.simulator.SimulationResults;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Stream;

@Stateless
public class GameService {
    private EventBus eventBus;

    private final static String GAME_CHANNEL = "/game/";

    private final Object matchUserLock = new Object();

    @EJB
    LoggedInUsers loggedInUsers;

    public Game createGame(User user1, User user2, GameOptions gameOptions) {
        Game game = new Game();

        HumanPlayer player1 = new HumanPlayer(user1);

        Player player2;

        if (gameOptions.isPlayAgainstComputer()) {
            //player2 = new StrategyBot(new VelocityStrategy(), this);
            player2 = new SimulatorBot(this);
            user2 = new User();
            user2.setUsername(player2.getPlayerName());
        } else {
            player2 = new HumanPlayer(user2);
        }

        List<Player> players = new ArrayList<>(2);
        players.add(player1);
        players.add(player2);

        user1.setCurrentPlayer(player1);
        user1.setCurrentGame(game);

        user2.setCurrentPlayer(player2);
        user2.setCurrentGame(game);

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        players.forEach(p -> p.setGame(game));

        Collections.shuffle(players);

        game.setPlayers(players);

        game.getCurrentPlayer().setFirstPlayer(true);

        game.gameLog("** Starting Game **");
        game.gameLog("Player 1: " + players.get(0).getPlayerName() + " - Player 2: " + players.get(1).getPlayerName());

        setupCards(game, gameOptions);

        game.startGame();

        return game;
    }

    public void setupCards(Game game, GameOptions gameOptions) {
        for (Player player : game.getPlayers()) {
            for (int i = 0; i < 8; i++) {
                player.addCardToDeck(new Scout());
            }

            player.addCardToDeck(new Viper());
            player.addCardToDeck(new Viper());

            player.setup();
        }

        List<Card> deck = new ArrayList<>();

        if (gameOptions.isIncludeBaseSet()) {
            deck.addAll(getBaseSetDeck());
            game.getCardSets().add(CardSet.CORE);
        }
        if (gameOptions.isIncludeColonyWars()) {
            deck.addAll(getColonyWarsDeck());
            game.getCardSets().add(CardSet.COLONY_WARS);
        }
        if (gameOptions.isIncludeYearOnePromos()) {
            deck.addAll(getYear1PromoCards());
            game.getCardSets().add(CardSet.PROMO_YEAR_1);
        }
        if (gameOptions.isIncludeCrisisBasesAndBattleships()) {
            deck.addAll(getCrisisBasesAndBattleships());
            game.getCardSets().add(CardSet.CRISIS_BASES_AND_BATTLESHIPS);
        }
        if (gameOptions.isIncludeCrisisEvents()) {
            deck.addAll(getCrisisEvents());
            game.getCardSets().add(CardSet.CRISIS_EVENTS);
        }
        if (gameOptions.isIncludeCrisisFleetsAndFortresses()) {
            deck.addAll(getCrisisFleetsAndFortresses());
            game.getCardSets().add(CardSet.CRISIS_FLEETS_AND_FORTRESSES);
        }
        if (gameOptions.isIncludeCrisisHeroes()) {
            deck.addAll(getCrisisHeroes());
            game.getCardSets().add(CardSet.CRISIS_HEROES);
        }
        if (gameOptions.isIncludeUnitedVarious()) {
            deck.addAll(getUnitedVarious());
            game.getCardSets().add(CardSet.UNITED_VARIOUS);
        }
        if (gameOptions.isIncludeUnitedShipsStationsAndPods()) {
            deck.addAll(getUnitedShipsStationsAndPods());
            game.getCardSets().add(CardSet.UNITED_SHIPS_STATIONS_AND_PODS);
        }
        if (gameOptions.isIncludeUnitedHeroes()) {
            deck.addAll(getUnitedHeroes());
            game.getCardSets().add(CardSet.UNITED_HEROES);
        }

        if (gameOptions.isIncludeGambits()) {
            addGambits(game);
            game.getCardSets().add(CardSet.GAMBITS);
        }

        Collections.shuffle(deck);

        game.setDeck(deck);

        if (!StringUtils.isEmpty(gameOptions.getStartingTradeRowCards())) {
            List<Card> tradeRow = new ArrayList<>();
            String[] cardsNames = gameOptions.getStartingTradeRowCards().split(",");
            Arrays.stream(cardsNames).forEach(cardName -> {
                Card card = getCardByName(cardName);
                if (card != null) {
                    tradeRow.add(card);
                }
            });
            game.setTradeRow(tradeRow);
            if (tradeRow.size() < 5) {
                game.addCardsToTradeRow(5 - tradeRow.size());
            }
        } else {
            game.addCardsToTradeRow(5);
        }

    }


    public List<Card> getBaseSetDeck() {
        List<Card> cards = new ArrayList<>();

        cards.add(new TradeBot());
        cards.add(new TradeBot());
        cards.add(new TradeBot());

        cards.add(new MissileBot());
        cards.add(new MissileBot());
        cards.add(new MissileBot());

        cards.add(new SupplyBot());
        cards.add(new SupplyBot());
        cards.add(new SupplyBot());

        cards.add(new PatrolMech());
        cards.add(new PatrolMech());

        cards.add(new StealthNeedle());

        cards.add(new BattleMech());

        cards.add(new MissileMech());

        cards.add(new BattleStation());
        cards.add(new BattleStation());

        cards.add(new MechWorld());

        cards.add(new BrainWorld());

        cards.add(new MachineBase());

        cards.add(new Junkyard());

        cards.add(new ImperialFighter());
        cards.add(new ImperialFighter());
        cards.add(new ImperialFighter());

        cards.add(new ImperialFrigate());
        cards.add(new ImperialFrigate());
        cards.add(new ImperialFrigate());

        cards.add(new SurveyShip());
        cards.add(new SurveyShip());
        cards.add(new SurveyShip());

        cards.add(new Corvette());
        cards.add(new Corvette());

        cards.add(new Battlecruiser());

        cards.add(new Dreadnaught());

        cards.add(new SpaceStation());
        cards.add(new SpaceStation());

        cards.add(new RecyclingStation());
        cards.add(new RecyclingStation());

        cards.add(new WarWorld());

        cards.add(new RoyalRedoubt());

        cards.add(new FleetHQ());

        cards.add(new FederationShuttle());
        cards.add(new FederationShuttle());
        cards.add(new FederationShuttle());

        cards.add(new Cutter());
        cards.add(new Cutter());
        cards.add(new Cutter());

        cards.add(new EmbassyYacht());
        cards.add(new EmbassyYacht());

        cards.add(new Freighter());
        cards.add(new Freighter());

        cards.add(new CommandShip());

        cards.add(new TradeEscort());

        cards.add(new Flagship());

        cards.add(new TradingPost());
        cards.add(new TradingPost());

        cards.add(new BarterWorld());
        cards.add(new BarterWorld());

        cards.add(new DefenseCenter());

        cards.add(new CentralOffice());

        cards.add(new PortOfCall());

        cards.add(new BlobFighter());
        cards.add(new BlobFighter());
        cards.add(new BlobFighter());

        cards.add(new TradePod());
        cards.add(new TradePod());
        cards.add(new TradePod());

        cards.add(new BattlePod());
        cards.add(new BattlePod());

        cards.add(new Ram());
        cards.add(new Ram());

        cards.add(new BlobDestroyer());
        cards.add(new BlobDestroyer());

        cards.add(new BattleBlob());

        cards.add(new BlobCarrier());

        cards.add(new Mothership());

        cards.add(new BlobWheel());
        cards.add(new BlobWheel());
        cards.add(new BlobWheel());

        cards.add(new TheHive());

        cards.add(new BlobWorld());

        return cards;
    }

    public List<Card> getYear1PromoCards() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Megahauler());

        cards.add(new TheArk());

        cards.add(new BattleBarge());
        cards.add(new BattleBarge());

        cards.add(new BattleScreecher());
        cards.add(new BattleScreecher());

        cards.add(new Starmarket());
        cards.add(new Starmarket());

        cards.add(new FortressOblivion());
        cards.add(new FortressOblivion());

        cards.add(new BreedingSite());

        cards.add(new StarbaseOmega());

        cards.add(new MercCruiser());
        cards.add(new MercCruiser());
        cards.add(new MercCruiser());

        return cards;
    }

    public List<Card> getCrisisBasesAndBattleships() {
        List<Card> cards = new ArrayList<>();

        cards.add(new MegaMech());

        cards.add(new DefenseBot());
        cards.add(new DefenseBot());

        cards.add(new ImperialTrader());

        cards.add(new FighterBase());
        cards.add(new FighterBase());

        cards.add(new Obliterator());

        cards.add(new TradeWheel());
        cards.add(new TradeWheel());

        cards.add(new ConstructionHauler());

        cards.add(new TradeRaft());
        cards.add(new TradeRaft());

        return cards;
    }

    public List<Card> getCrisisEvents() {
        List<Card> cards = new ArrayList<>();

        cards.add(new BlackHole());

        cards.add(new Bombardment());

        cards.add(new Comet());
        cards.add(new Comet());

        cards.add(new GalacticSummit());

        cards.add(new Quasar());
        cards.add(new Quasar());

        cards.add(new Supernova());

        cards.add(new TradeMission());
        cards.add(new TradeMission());

        cards.add(new WarpJump());
        cards.add(new WarpJump());

        return cards;
    }

    public List<Card> getCrisisFleetsAndFortresses() {
        List<Card> cards = new ArrayList<>();

        cards.add(new BorderFort());

        cards.add(new PatrolBot());
        cards.add(new PatrolBot());

        cards.add(new StarFortress());

        cards.add(new CargoLaunch());
        cards.add(new CargoLaunch());

        cards.add(new DeathWorld());

        cards.add(new SpikePod());
        cards.add(new SpikePod());

        cards.add(new CapitolWorld());

        cards.add(new CustomsFrigate());
        cards.add(new CustomsFrigate());

        return cards;
    }

    public List<Hero> getCrisisHeroes() {
        List<Hero> cards = new ArrayList<>();

        cards.add(new RamPilot());
        cards.add(new RamPilot());

        cards.add(new BlobOverlord());

        cards.add(new SpecialOpsDirector());
        cards.add(new SpecialOpsDirector());

        cards.add(new CeoTorres());

        cards.add(new WarElder());
        cards.add(new WarElder());

        cards.add(new HighPriestLyle());

        cards.add(new CunningCaptain());
        cards.add(new CunningCaptain());

        cards.add(new AdmiralRasmussen());

        return cards;
    }

    public List<Card> getColonyWarsDeck() {
        List<Card> cards = new ArrayList<>();

        cards.add(new SolarSkiff());
        cards.add(new SolarSkiff());
        cards.add(new SolarSkiff());

        cards.add(new TradeHauler());
        cards.add(new TradeHauler());
        cards.add(new TradeHauler());

        cards.add(new PatrolCutter());
        cards.add(new PatrolCutter());
        cards.add(new PatrolCutter());

        cards.add(new FrontierFerry());
        cards.add(new FrontierFerry());

        cards.add(new ColonySeedShip());

        cards.add(new Peacekeeper());

        cards.add(new StorageSilo());
        cards.add(new StorageSilo());

        cards.add(new CentralStation());
        cards.add(new CentralStation());

        cards.add(new FederationShipyard());

        cards.add(new LoyalColony());

        cards.add(new FactoryWorld());

        cards.add(new StarBarge());
        cards.add(new StarBarge());
        cards.add(new StarBarge());

        cards.add(new Lancer());
        cards.add(new Lancer());
        cards.add(new Lancer());

        cards.add(new Falcon());
        cards.add(new Falcon());

        cards.add(new Gunship());
        cards.add(new Gunship());

        cards.add(new HeavyCruiser());

        cards.add(new AgingBattleship());

        cards.add(new EmperorsDreadnaught());

        cards.add(new OrbitalPlatform());
        cards.add(new OrbitalPlatform());
        cards.add(new OrbitalPlatform());

        cards.add(new CommandCenter());
        cards.add(new CommandCenter());

        cards.add(new SupplyDepot());

        cards.add(new ImperialPalace());

        cards.add(new Swarmer());
        cards.add(new Swarmer());
        cards.add(new Swarmer());

        cards.add(new Predator());
        cards.add(new Predator());
        cards.add(new Predator());

        cards.add(new Swarmer());
        cards.add(new Swarmer());
        cards.add(new Swarmer());

        cards.add(new Ravager());
        cards.add(new Ravager());

        cards.add(new Parasite());

        cards.add(new Moonwurm());

        cards.add(new Leviathan());

        cards.add(new StellarReef());
        cards.add(new StellarReef());
        cards.add(new StellarReef());

        cards.add(new Bioformer());
        cards.add(new Bioformer());

        cards.add(new PlasmaVent());

        cards.add(new BattleBot());
        cards.add(new BattleBot());
        cards.add(new BattleBot());

        cards.add(new RepairBot());
        cards.add(new RepairBot());
        cards.add(new RepairBot());

        cards.add(new ConvoyBot());
        cards.add(new ConvoyBot());
        cards.add(new ConvoyBot());

        cards.add(new MiningMech());
        cards.add(new MiningMech());

        cards.add(new MechCruiser());

        cards.add(new TheWrecker());

        cards.add(new WarningBeacon());
        cards.add(new WarningBeacon());
        cards.add(new WarningBeacon());

        cards.add(new TheOracle());

        cards.add(new StealthTower());

        cards.add(new FrontierStation());

        cards.add(new TheIncinerator());

        return cards;
    }

    public List<Card> getUnitedVarious() {
        List<Card> cards = new ArrayList<>();

        cards.add(new BlobBot());
        cards.add(new BlobBot());

        cards.add(new TradeStar());
        cards.add(new TradeStar());

        cards.add(new AllianceTransport());
        cards.add(new AllianceTransport());

        cards.add(new CoalitionMessenger());
        cards.add(new CoalitionMessenger());

        cards.add(new UnionStronghold());

        cards.add(new EmbassyBase());

        cards.add(new LookoutPost());

        cards.add(new ExchangePoint());

        return cards;
    }

    public List<Card> getUnitedShipsStationsAndPods() {
        List<Card> cards = new ArrayList<>();

        cards.add(new CoalitionFreighter());
        cards.add(new CoalitionFreighter());

        cards.add(new AllianceFrigate());
        cards.add(new AllianceFrigate());

        cards.add(new AssaultPod());
        cards.add(new AssaultPod());

        cards.add(new UnityFighter());
        cards.add(new UnityFighter());

        cards.add(new CoalitionFortress());

        cards.add(new AllianceLanding());

        cards.add(new UnityStation());

        cards.add(new UnionCluster());

        return cards;
    }

    public List<Hero> getUnitedHeroes() {
        List<Hero> cards = new ArrayList<>();

        cards.add(new CommanderKlik());
        cards.add(new CommanderKlik());

        cards.add(new Screecher());
        cards.add(new Screecher());

        cards.add(new ChairmanHaygan());
        cards.add(new ChairmanHaygan());

        cards.add(new ChancellorHartman());
        cards.add(new ChancellorHartman());

        cards.add(new CEOShaner());

        cards.add(new CommanderZhang());

        cards.add(new ConfessorMorris());

        cards.add(new HiveLord());

        return cards;
    }

    public List<Gambit> getGambits() {
        List<Gambit> gambits = new ArrayList<>();

        gambits.add(new BoldRaid());

        gambits.add(new EnergyShield());

        gambits.add(new FrontierFleet());

        gambits.add(new PoliticalManeuver());

        gambits.add(new RiseToPower());

        gambits.add(new SalvageOperation());

        gambits.add(new SmugglingRun());

        gambits.add(new SurpriseAssault());

        gambits.add(new UnlikelyAlliance());

        return gambits;
    }

    private void addGambits(Game game) {
        List<Gambit> gambits = getGambits();
        Collections.shuffle(gambits);

        int i = 0;

        for (Player player : game.getPlayers()) {
            player.getGambits().add(gambits.get(i));
            game.gameLog(player.getPlayerName() + " starts with gambit " + gambits.get(i).getName());
            i++;
            player.getGambits().add(gambits.get(i));
            game.gameLog(player.getPlayerName() + " starts with gambit " + gambits.get(i).getName());
            i++;
        }
    }

    public List<Gambit> getGambitsFromGambitNames(String gambitNames) {
        List<Gambit> gambits = new ArrayList<>();

        String[] gambitNameArray = gambitNames.split(",");

        for (String gambitName : gambitNameArray) {
            Gambit gambit = getGambitFromName(gambitName);
            if (gambit == null) {
                System.out.println("Gambit not found for: " + gambitName);
            } else {
                gambits.add(gambit);
            }
        }

        return gambits;
    }

    public Gambit getGambitFromName(String gambitName) {
        gambitName = gambitName.replaceAll("\\s", "").toLowerCase();

        switch (gambitName) {
            case "bolrai":
            case "boldraid":
                return new BoldRaid();
            case "eneshi":
            case "energyshield":
                return new EnergyShield();
            case "frofle":
            case "frontierfleet":
                return new FrontierFleet();
            case "polman":
            case "politicalmaneuver":
                return new PoliticalManeuver();
            case "ristopow":
            case "risetopower":
                return new RiseToPower();
            case "salope":
            case "salvageoperation":
                return new SalvageOperation();
            case "smurun":
            case "smugglingrun":
                return new SmugglingRun();
            case "surass":
            case "surpriseassault":
                return new SurpriseAssault();
            case "unlall":
            case "unlikelyalliance":
                return new UnlikelyAlliance();
            default:
                return null;
        }
    }

    public List<Card> getCardsFromCardNames(String cardNames) {
        List<Card> cards = new ArrayList<>();

        if (cardNames == null || cardNames.isEmpty()) {
            return cards;
        }

        String[] cardNameArray = cardNames.split(",");

        for (String cardName : cardNameArray) {
            int multiplier = 1;
            String cardNameWithoutMultiplier = cardName;
            if (cardName.contains("*")) {
                cardNameWithoutMultiplier = cardName.substring(0, cardName.indexOf("*"));
                multiplier = Integer.parseInt(cardName.substring(cardName.indexOf("*") + 1).trim());
            }
            Card card = getCardFromName(cardNameWithoutMultiplier);
            if (card == null) {
                System.out.println("Card not found for: " + cardName);
            } else {
                if (multiplier == 1) {
                    cards.add(card);
                } else {
                    for (int i = 1; i <= multiplier; i++) {
                        try {
                            cards.add(card.getClass().newInstance());
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return cards;
    }

    public List<Base> getBasesFromCardNames(String cardNames) {
        List<Base> bases = new ArrayList<>();

        if (cardNames == null || cardNames.isEmpty()) {
            return bases;
        }

        String[] cardNameArray = cardNames.split(",");

        for (String cardName : cardNameArray) {
            Card card = getCardFromName(cardName);
            if (card == null) {
                System.out.println("Base not found for: " + cardName);
            } else {
                bases.add((Base) card);
            }
        }

        return bases;
    }

    public Card getCardFromName(String cardName) {
        cardName = cardName.replaceAll("\\s", "").toLowerCase();
        cardName = cardName.replaceAll("'", "");

        switch (cardName) {
            case "agibat":
            case "agingb":
            case "agingbattleship":
                return new AgingBattleship();

            case "barwor":
            case "barterw":
            case "barterworld":
                return new BarterWorld();

            case "batbar":
            case "bbarge":
            case "battlebarge":
                return new BattleBarge();

            case "batblo":
            case "bblob":
            case "battleb":
            case "battleblob":
                return new BattleBlob();

            case "batbot":
            case "bbot":
            case "battlebot":
                return new BattleBot();

            case "batcru":
            case "bcr":
            case "bcruiser":
            case "battlecruiser":
                return new Battlecruiser();

            case "biofor":
            case "biof":
            case "bioformer":
                return new Bioformer();

            case "batmec":
            case "bm":
            case "bmech":
            case "battlem":
            case "battlemech":
                return new BattleMech();

            case "batpod":
            case "bp":
            case "bpod":
            case "battlepod":
                return new BattlePod();

            case "batscr":
            case "bscreecher":
            case "battlescreecher":
                return new BattleScreecher();

            case "batsta":
            case "bs":
            case "bstation":
            case "battles":
            case "battlestation":
                return new BattleStation();

            case "blahol":
            case "blackhole":
                return new BlackHole();

            case "blocar":
            case "bcarrier":
            case "blobcarrier":
                return new BlobCarrier();

            case "blodes":
            case "bdestroyer":
            case "blobdestroyer":
                return new BlobDestroyer();

            case "blofig":
            case "bf":
            case "bfighter":
            case "blobf":
            case "blobfighter":
                return new BlobFighter();

            case "blowhe":
            case "bwheel":
            case "blobwheel":
                return new BlobWheel();

            case "blowor":
            case "bw":
            case "bworld":
            case "blobw":
            case "blobworld":
                return new BlobWorld();

            case "bom":
            case "bombardment":
                return new Bombardment();

            case "borfor":
            case "bfort":
            case "borderfort":
                return new BorderFort();

            case "brawor":
            case "brainw":
            case "brainworld":
                return new BrainWorld();

            case "bresit":
            case "breedings":
            case "breedingsite":
                return new BreedingSite();

            case "capwor":
            case "capitolw":
            case "capitolworld":
                return new CapitolWorld();

            case "carlau":
            case "cargolaunch":
                return new CargoLaunch();

            case "carpod":
            case "cargop":
            case "cargopod":
                return new CargoPod();

            case "cenoff":
            case "centraloffice":
                return new CentralOffice();

            case "censta":
            case "centrals":
            case "cstation":
            case "centralstation":
                return new CentralStation();

            case "colseeshi":
            case "colonyseeds":
            case "colonyseedship":
                return new ColonySeedShip();

            case "com":
            case "comet":
                return new Comet();

            case "comcen":
            case "ccenter":
            case "commandc":
            case "commandcenter":
                return new CommandCenter();

            case "comshi":
            case "cs":
            case "commands":
            case "commandship":
                return new CommandShip();

            case "conhau":
            case "constructionhauler":
                return new ConstructionHauler();

            case "conbot":
            case "convoyb":
            case "convoybot":
                return new ConvoyBot();

            case "cor":
            case "corvette":
                return new Corvette();

            case "cusfri":
            case "customsfrigate":
                return new CustomsFrigate();

            case "cut":
            case "cutter":
                return new Cutter();

            case "deawor":
            case "deathw":
            case "deathworld":
                return new DeathWorld();

            case "defbot":
            case "db":
            case "dbot":
            case "defenseb":
            case "defensebot":
                return new DefenseBot();

            case "defcen":
            case "dc":
            case "dcenter":
            case "defensecenter":
                return new DefenseCenter();

            case "dre":
            case "dreadnaught":
                return new Dreadnaught();

            case "embyac":
            case "embassyyacht":
                return new EmbassyYacht();

            case "empdre":
            case "emperorsdreadnaught":
                return new EmperorsDreadnaught();

            case "exp":
            case "e":
            case "explorer":
                return new Explorer();

            case "facwor":
            case "factoryw":
            case "factoryworld":
                return new FactoryWorld();

            case "fal":
            case "falcon":
                return new Falcon();

            case "fedshi":
            case "fshipyard":
            case "federationshipyard":
                return new FederationShipyard();

            case "fedshu":
            case "fs":
            case "fshuttle":
            case "federationshuttle":
                return new FederationShuttle();

            case "figbas":
            case "fighterb":
            case "fighterbase":
                return new FighterBase();

            case "fla":
            case "flags":
            case "flagship":
                return new Flagship();

            case "flehq":
            case "fhq":
            case "fleethq":
                return new FleetHQ();

            case "forobl":
            case "fortressoblivion":
                return new FortressOblivion();

            case "fre":
            case "freighter":
                return new Freighter();

            case "frofer":
            case "fferry":
            case "frontierferry":
                return new FrontierFerry();

            case "frosta":
            case "frontiers":
            case "fstation":
            case "frontierstation":
                return new FrontierStation();

            case "galsum":
            case "galacticsummit":
                return new GalacticSummit();

            case "gun":
            case "guns":
            case "gunship":
                return new Gunship();

            case "heacru":
            case "heavyc":
            case "heavycruiser":
                return new HeavyCruiser();

            case "impfig":
            case "if":
            case "ifighter":
            case "imperialf":
            case "imperialfighter":
                return new ImperialFighter();

            case "impfri":
            case "ifrigate":
            case "imperialfrigate":
                return new ImperialFrigate();

            case "imppal":
            case "ipalace":
            case "imperialpalace":
                return new ImperialPalace();

            case "imptra":
            case "itrader":
            case "imperialtrader":
            case "imperialt":
                return new ImperialTrader();

            case "jun":
            case "junkyard":
                return new Junkyard();

            case "lan":
            case "lancer":
                return new Lancer();

            case "lev":
            case "leviathan":
                return new Leviathan();

            case "loycol":
            case "loyalc":
            case "loyalcolony":
                return new LoyalColony();

            case "macbas":
            case "machineb":
            case "machinebase":
                return new MachineBase();

            case "meccru":
            case "mechc":
            case "mcruiser":
            case "mechcruiser":
                return new MechCruiser();

            case "mecwor":
            case "mw":
            case "mworld":
            case "mechw":
            case "mechworld":
                return new MechWorld();

            case "meg":
            case "megahauler":
                return new Megahauler();

            case "megmec":
            case "megam":
            case "megamech":
                return new MegaMech();

            case "mercru":
            case "mercc":
            case "merccruiser":
                return new MercCruiser();

            case "minmec":
            case "miningm":
            case "miningmech":
                return new MiningMech();

            case "misbot":
            case "missileb":
            case "missilebot":
                return new MissileBot();

            case "mismec":
            case "missilem":
            case "missilemech":
                return new MissileMech();

            case "moo":
            case "moonw":
            case "moonworm":
            case "moonwurm":
                return new Moonwurm();

            case "mot":
            case "mothers":
            case "mothership":
                return new Mothership();

            case "obl":
            case "obliterator":
                return new Obliterator();

            case "orbpla":
            case "orbitalplatform":
                return new OrbitalPlatform();

            case "par":
            case "parasite":
                return new Parasite();

            case "patbot":
            case "pb":
            case "patrolb":
            case "patrolbot":
                return new PatrolBot();

            case "patcut":
            case "pcutter":
            case "patrolcutter":
                return new PatrolCutter();

            case "patmec":
            case "pm":
            case "patrolm":
            case "patrolmech":
                return new PatrolMech();

            case "pea":
            case "peacekeeper":
                return new Peacekeeper();

            case "plaven":
            case "plasmavent":
                return new PlasmaVent();

            case "porofcal":
            case "poc":
            case "portofcall":
                return new PortOfCall();

            case "pre":
            case "predator":
                return new Predator();

            case "qua":
            case "quasaar":
                return new Quasar();

            case "ram":
                return new Ram();

            case "rav":
            case "ravager":
                return new Ravager();

            case "recsta":
            case "recyclings":
            case "recyclingstation":
                return new RecyclingStation();

            case "repbot":
            case "repairb":
            case "repairbot":
                return new RepairBot();

            case "royred":
            case "royalredoubt":
                return new RoyalRedoubt();

            case "sco":
            case "s":
            case "scout":
                return new Scout();

            case "solski":
            case "solarskiff":
                return new SolarSkiff();

            case "spasta":
            case "spaces":
            case "spacestation":
                return new SpaceStation();

            case "spipod":
            case "spikep":
            case "spikepod":
                return new SpikePod();

            case "stabar":
            case "sbarge":
            case "starbarge":
                return new StarBarge();

            case "staome":
            case "somega":
            case "starbaseomega":
                return new StarbaseOmega();

            case "stafor":
            case "sfortress":
            case "starfortress":
                return new StarFortress();

            case "sta":
            case "stamar":
            case "smarket":
            case "starmarket":
                return new Starmarket();

            case "stenee":
            case "stealthneedle":
                return new StealthNeedle();

            case "stetow":
            case "stealthtower":
                return new StealthTower();

            case "steree":
            case "stellarreef":
                return new StellarReef();

            case "stosil":
            case "storagesilo":
                return new StorageSilo();

            case "sup":
            case "supernova":
                return new Supernova();

            case "supbot":
            case "sb":
            case "supplyb":
            case "supplybot":
                return new SupplyBot();

            case "supdep":
            case "sdepot":
            case "supplydepot":
                return new SupplyDepot();

            case "surshi":
            case "surveys":
            case "surveyship":
                return new SurveyShip();

            case "sw":
            case "swarmer":
                return new Swarmer();

            case "ark":
            case "theark":
                return new TheArk();

            case "thehiv":
            case "hive":
            case "thehive":
                return new TheHive();

            case "theinc":
            case "incinerator":
            case "theincinerator":
                return new TheIncinerator();

            case "theora":
            case "oracle":
            case "theoracle":
                return new TheOracle();

            case "thewre":
            case "wrecker":
            case "thewrecker":
                return new TheWrecker();

            case "trabot":
            case "tb":
            case "tbot":
            case "tradebot":
                return new TradeBot();

            case "traesc":
            case "tescort":
            case "tradeescort":
                return new TradeEscort();

            case "trahau":
            case "thauler":
            case "tradehauler":
                return new TradeHauler();

            case "tramis":
            case "tmission":
            case "trademission":
                return new TradeMission();

            case "trapod":
            case "tp":
            case "tpod":
            case "tradepod":
                return new TradePod();

            case "traraf":
            case "traft":
            case "traderaft":
                return new TradeRaft();

            case "trawhe":
            case "twheel":
            case "tradewheel":
                return new TradeWheel();

            case "trapos":
            case "tpost":
            case "tradingpost":
                return new TradingPost();

            case "vip":
            case "v":
            case "viper":
                return new Viper();

            case "warbea":
            case "warningbeacon":
                return new WarningBeacon();

            case "warjum":
            case "warpjump":
                return new WarpJump();

            case "warwor":
            case "warw":
            case "warworld":
                return new WarWorld();

            case "blobbot":
            case "blobot":
                return new BlobBot();

            case "tradestar":
            case "trasta":
                return new TradeStar();

            case "alliancetransport":
            case "alltra":
                return new AllianceTransport();

            case "coalitionmessenger":
            case "coames":
                return new CoalitionMessenger();

            case "unionstronghold":
            case "unistr":
                return new UnionStronghold();

            case "embassybase":
            case "embbas":
                return new EmbassyBase();

            case "lookoutpost":
            case "loopos":
                return new LookoutPost();

            case "exchangepoint":
            case "excpoi":
                return new ExchangePoint();

            case "coalitionfreighter":
            case "coafre":
                return new CoalitionFreighter();

            case "alliancefrigate":
            case "allfri":
                return new AllianceFrigate();

            case "assaultpod":
            case "asspod":
                return new AssaultPod();

            case "unityfighter":
            case "unifig":
                return new UnityFighter();

            case "coalitionfortress":
            case "coafor":
                return new CoalitionFortress();

            case "alliancelanding":
            case "alllan":
                return new AllianceLanding();

            case "unitystation":
            case "unista":
                return new UnityStation();

            case "unioncluster":
            case "uniclu":
                return new UnionCluster();

            default:
                return null;
        }
    }

    public List<Hero> getHeroesFromHeroNames(String heroNames) {
        List<Hero> heroes = new ArrayList<>();

        String[] heroNameArray = heroNames.split(",");

        for (String heroName : heroNameArray) {
            Hero hero = getHeroFromName(heroName);
            if (hero == null) {
                System.out.println("Hero not found for: " + heroName);
            } else {
                heroes.add(hero);
            }
        }

        return heroes;
    }

    public Hero getHeroFromName(String heroName) {
        heroName = heroName.replaceAll("\\s", "").toLowerCase();

        switch (heroName) {
            case "ar":
            case "admiralrasmussen":
                return new AdmiralRasmussen();
            case "bo":
            case "boverlord":
            case "bloboverlord":
                return new BlobOverlord();
            case "ct":
            case "ceotorres":
                return new CeoTorres();
            case "cc":
            case "cunningcaptain":
                return new CunningCaptain();
            case "hpl":
            case "highpriestlyle":
                return new HighPriestLyle();
            case "rp":
            case "rampilot":
                return new RamPilot();
            case "sod":
            case "specialopsdirector":
                return new SpecialOpsDirector();
            case "we":
            case "warelder":
                return new WarElder();

            case "commanderklik":
            case "comkli":
                return new CommanderKlik();

            case "screecher":
            case "scr":
                return new Screecher();

            case "chairmanhaygan":
            case "chahay":
                return new ChairmanHaygan();

            case "chancellorhartman":
            case "chahar":
                return new ChancellorHartman();

            case "ceoshaner":
            case "ceosha":
                return new CEOShaner();

            case "commanderzhang":
            case "comzha":
                return new CommanderZhang();

            case "confessormorris":
            case "conmor":
                return new ConfessorMorris();

            case "hivelord":
            case "hivlor":
                return new HiveLord();

            default:
                return null;
        }
    }

    public Card getCardByName(String cardName) {
        if (cardName == null) {
            return null;
        }

        cardName = cardName.replaceAll("\\s", "").toLowerCase();
        cardName = cardName.replaceAll("'", "");

        Card card = getCardFromName(cardName);

        if (card == null) {
            card = getHeroFromName(cardName);
        }

        if (card == null) {
            card = getGambitFromName(cardName);
        }

        return card;
    }

    public void autoMatchUser(User user) {
        synchronized (matchUserLock) {
            if (user.getCurrentGame() != null) {
                return;
            }
            User opponent = getMatchingUser(user);
            if (opponent != null) {
                opponent.setAutoMatch(false);
                user.setAutoMatch(false);
                GameOptions gameOptions;
                if (!user.getGameOptions().isCustomGameOptions()) {
                    gameOptions = opponent.getGameOptions();
                } else {
                    gameOptions = user.getGameOptions();
                }
                createGame(user, opponent, gameOptions);

                sendLobbyMessage(user.getUsername(), opponent.getUsername(), "game_started");
            } else {
                user.setAutoMatch(true);
            }
        }
    }

    public void playAgainstComputer(User user) {
        if (user.getCurrentGame() != null) {
            return;
        }

        createGame(user, null, user.getGameOptions());
    }

    private User getMatchingUser(User user) {
        List<User> users = loggedInUsers.getUsersWaitingForAutoMatch();
        if (!users.isEmpty()) {
            for (User opponent : users) {
                if (!user.getGameOptions().isCustomGameOptions() || !opponent.getGameOptions().isCustomGameOptions() || opponent.getGameOptions().equals(user.getGameOptions())) {
                    return opponent;
                }
            }
        }
        return null;
    }

    public void sendLobbyMessage(String sender, String recipient, String message) {
        sendGameMessage(sender, recipient, "lobby", message);
    }

    public void sendLobbyMessageToAll(String sender, String message) {
        sendLobbyMessage(sender, "*", message);
    }

    public void refreshLobby(String sender) {
        sendLobbyMessageToAll(sender, "refresh_lobby");
    }

    public void sendGameMessage(String sender, String recipient, String channel, String message) {
        getEventBus().publish(GAME_CHANNEL + channel + "/" + recipient, sender + ":" + message);
    }

    EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = EventBusFactory.getDefault().eventBus();
        }
        return eventBus;
    }

    public BotStrategy determineStrategyBasedOnCards(List<Card> cards) {
        //todo
        return new VelocityStrategy();
    }

    public Map<BotStrategy, Float> simulateBestStrategy(Game originalGame, int timesToSimulate) {
        Game copiedGame = originalGame.copyGameForSimulation();

        BotStrategy opponentStrategy = determineStrategyBasedOnCards(originalGame.getCurrentPlayer().getOpponent().getAllCards());

        Map<BotStrategy, Float> strategyResults = new LinkedHashMap<>();

        List<BotStrategy> strategies = new ArrayList<>();

        strategies.add(new AttackStrategy());
        strategies.add(new AttackVelocityStrategy());
        strategies.add(new DefenseStrategy());
        strategies.add(new DefenseVelocityStrategy());
        strategies.add(new EconomyStrategy());
        strategies.add(new VelocityStrategy());

        for (BotStrategy strategy : strategies) {
            StrategyBot strategyBot = new StrategyBot(strategy, this, originalGame.getCurrentPlayer(), copiedGame);
            StrategyBot opponentBot = new StrategyBot(opponentStrategy, this, strategyBot, copiedGame);

            strategyBot.setOpponent(opponentBot);
            opponentBot.setOpponent(strategyBot);

            List<Player> players = new ArrayList<>();

            if (originalGame.getCurrentPlayer().isFirstPlayer()) {
                players.add(strategyBot);
                players.add(opponentBot);
            } else {
                players.add(opponentBot);
                players.add(strategyBot);
            }

            copiedGame.setPlayers(players);

            SimulationResults results = simulateGameToEnd(copiedGame, timesToSimulate, null, true, false);

            strategyResults.put(strategy, results.getWinPercentage());
        }

        return strategyResults;
    }

    public SimulationResults simulateGameToEnd(Game copiedGame, int timesToSimulate, Card cardToBuyThisTurn,
                                               boolean simulatingBestBot, boolean simulatingBestCardToBuy) {

        SimulationResults results = new SimulationResults();

        boolean createdWinGameLog = false;
        boolean createdLossGameLog = false;

        int totalGamesCounted = 0;

        List<Game> games = new ArrayList<>(timesToSimulate);

        Map<String, Map<Integer, Integer>> averageAuthorityByPlayerByTurn = new HashMap<>();

        LinkedHashMap<String, Integer> playerWinDifferentialByCardsAtEndOfGame = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> opponentWinDifferentialByCardsAtEndOfGame = new LinkedHashMap<>();

        LinkedHashMap<String, Float> playerWinPercentageByFirstDeckCard = new LinkedHashMap<>();
        LinkedHashMap<String, Float> opponentWinPercentageByFirstDeckCard = new LinkedHashMap<>();

        Map<String, Integer> playerWinsByFirstDeckCard = new HashMap<>();
        Map<String, Integer> playerTotalGamesByFirstDeckCard = new HashMap<>();

        Map<String, Integer> opponentWinsByFirstDeckCard = new HashMap<>();
        Map<String, Integer> opponentTotalGamesByFirstDeckCard = new HashMap<>();

        LinkedHashMap<String, Float> playerWinPercentageBySecondDeckCard = new LinkedHashMap<>();
        LinkedHashMap<String, Float> opponentWinPercentageBySecondDeckCard = new LinkedHashMap<>();

        Map<String, Integer> playerWinsBySecondDeckCard = new HashMap<>();
        Map<String, Integer> playerTotalGamesBySecondDeckCard = new HashMap<>();

        Map<String, Integer> opponentWinsBySecondDeckCard = new HashMap<>();
        Map<String, Integer> opponentTotalGamesBySecondDeckCard = new HashMap<>();

        LinkedHashMap<String, Float> playerWinPercentageByNumScoutsFirstTwoHands = new LinkedHashMap<>();
        LinkedHashMap<String, Float> opponentWinPercentageByNumScoutsFirstTwoHands = new LinkedHashMap<>();

        Map<String, Integer> playerWinsByNumScoutsFirstTwoHands = new HashMap<>();
        Map<String, Integer> playerTotalGamesByNumScoutsFirstTwoHands = new HashMap<>();

        Map<String, Integer> opponentWinsByNumScoutsFirstTwoHands = new HashMap<>();
        Map<String, Integer> opponentTotalGamesByNumScoutsFirstTwoHands = new HashMap<>();

        float turnTotal = 0;

        int wins = 0;

        Player player = copiedGame.getCurrentPlayer();
        player.setPlayerName(player.getClass().getSimpleName() + "(Player)");
        averageAuthorityByPlayerByTurn.put(player.getPlayerName(), new HashMap<>());

        Player opponent = copiedGame.getCurrentPlayer().getOpponent();
        opponent.setPlayerName(opponent.getClass().getSimpleName() + "(Opponent)");
        averageAuthorityByPlayerByTurn.put(opponent.getPlayerName(), new HashMap<>());

        for (int i = 0; i < timesToSimulate; i++) {
            System.out.println("Simulating game " + i);
            boolean createGameLog = !createdWinGameLog || !createdLossGameLog;
            Game game = simulateGameToEnd(copiedGame, createGameLog, cardToBuyThisTurn);
            if (game == null || cardToBuyThisTurn != null && !game.getWinner().isBoughtSpecifiedCardOnFirstTurn() && !game.getLoser().isBoughtSpecifiedCardOnFirstTurn()) {
                continue;
            }

            LinkedHashMap<String, Integer> winnerWinDifferentialMap;
            LinkedHashMap<String, Integer> loserWinDifferentialMap;

            Map<String, Integer> winnerFirstDeckWinsMap;
            Map<String, Integer> winnerFirstDeckTotalGamesMap;
            Map<String, Integer> loserFirstDeckTotalGamesMap;

            Map<String, Integer> winnerSecondDeckWinsMap;
            Map<String, Integer> winnerSecondDeckTotalGamesMap;
            Map<String, Integer> loserSecondDeckTotalGamesMap;

            Map<String, Integer> winnerNumScoutsFirstTwoHandsWinsMap;
            Map<String, Integer> winnerNumScoutsFirstTwoHandsTotalGamesMap;
            Map<String, Integer> loserNumScoutsFirstTwoHandsTotalGamesMap;

            if (game.getWinner().getPlayerName().equals(player.getPlayerName())) {
                winnerWinDifferentialMap = playerWinDifferentialByCardsAtEndOfGame;
                loserWinDifferentialMap = opponentWinDifferentialByCardsAtEndOfGame;

                winnerFirstDeckWinsMap = playerWinsByFirstDeckCard;
                winnerFirstDeckTotalGamesMap = playerTotalGamesByFirstDeckCard;
                loserFirstDeckTotalGamesMap = opponentTotalGamesByFirstDeckCard;

                winnerSecondDeckWinsMap = playerWinsBySecondDeckCard;
                winnerSecondDeckTotalGamesMap = playerTotalGamesBySecondDeckCard;
                loserSecondDeckTotalGamesMap = opponentTotalGamesBySecondDeckCard;

                winnerNumScoutsFirstTwoHandsWinsMap = playerWinsByNumScoutsFirstTwoHands;
                winnerNumScoutsFirstTwoHandsTotalGamesMap = playerTotalGamesByNumScoutsFirstTwoHands;
                loserNumScoutsFirstTwoHandsTotalGamesMap = opponentTotalGamesByNumScoutsFirstTwoHands;

                wins++;
                if (createGameLog) {
                    if (!createdWinGameLog) {
                        results.setWinGameLog(game.getGameLog().toString());
                        createdWinGameLog = true;
                    }
                    game.setGameLog(null);
                }
            } else {
                winnerWinDifferentialMap = opponentWinDifferentialByCardsAtEndOfGame;
                loserWinDifferentialMap = playerWinDifferentialByCardsAtEndOfGame;

                winnerFirstDeckWinsMap = opponentWinsByFirstDeckCard;
                winnerFirstDeckTotalGamesMap = opponentTotalGamesByFirstDeckCard;
                loserFirstDeckTotalGamesMap = playerTotalGamesByFirstDeckCard;

                winnerSecondDeckWinsMap = opponentWinsBySecondDeckCard;
                winnerSecondDeckTotalGamesMap = opponentTotalGamesBySecondDeckCard;
                loserSecondDeckTotalGamesMap = playerTotalGamesBySecondDeckCard;

                winnerNumScoutsFirstTwoHandsWinsMap = opponentWinsByNumScoutsFirstTwoHands;
                winnerNumScoutsFirstTwoHandsTotalGamesMap = opponentTotalGamesByNumScoutsFirstTwoHands;
                loserNumScoutsFirstTwoHandsTotalGamesMap = playerTotalGamesByNumScoutsFirstTwoHands;

                if (!createdLossGameLog) {
                    results.setLossGameLog(game.getGameLog().toString());
                    createdLossGameLog = true;
                }
                game.setGameLog(null);
            }

            if (!simulatingBestBot && !simulatingBestCardToBuy) {
                String winnerStartingScoutsSplit = "";
                winnerStartingScoutsSplit += game.getWinner().getScoutsInFirstHand() + "/" + game.getWinner().getScoutsInSecondHand();
                Integer winsByStartingScouts = winnerNumScoutsFirstTwoHandsWinsMap.get(winnerStartingScoutsSplit);
                if (winsByStartingScouts == null) {
                    winsByStartingScouts = 1;
                } else {
                    winsByStartingScouts++;
                }
                winnerNumScoutsFirstTwoHandsWinsMap.put(winnerStartingScoutsSplit, winsByStartingScouts);
                Integer winnerTotalGamesByStartingScouts = winnerNumScoutsFirstTwoHandsTotalGamesMap.get(winnerStartingScoutsSplit);
                if (winnerTotalGamesByStartingScouts == null) {
                    winnerTotalGamesByStartingScouts = 1;
                } else {
                    winnerTotalGamesByStartingScouts++;
                }
                winnerNumScoutsFirstTwoHandsTotalGamesMap.put(winnerStartingScoutsSplit, winnerTotalGamesByStartingScouts);

                String loserStartingScoutsSplit = "";
                loserStartingScoutsSplit += game.getLoser().getScoutsInFirstHand() + "/" + game.getLoser().getScoutsInSecondHand();
                Integer loserTotalGamesByStartingScouts = loserNumScoutsFirstTwoHandsTotalGamesMap.get(loserStartingScoutsSplit);
                if (loserTotalGamesByStartingScouts == null) {
                    loserTotalGamesByStartingScouts = 1;
                } else {
                    loserTotalGamesByStartingScouts++;
                }
                loserNumScoutsFirstTwoHandsTotalGamesMap.put(loserStartingScoutsSplit, loserTotalGamesByStartingScouts);

                game.getWinner().getAllCards().forEach(c -> {
                    if (!(c instanceof Scout || c instanceof Viper)) {
                        Integer winDifferential = winnerWinDifferentialMap.get(c.getName());
                        if (winDifferential == null) {
                            winDifferential = 1;
                        } else {
                            winDifferential++;
                        }
                        winnerWinDifferentialMap.put(c.getName(), winDifferential);
                    }
                });

                game.getLoser().getAllCards().forEach(c -> {
                    if (!(c instanceof Scout || c instanceof Viper)) {
                        Integer winDifferential = loserWinDifferentialMap.get(c.getName());
                        if (winDifferential == null) {
                            winDifferential = -1;
                        } else {
                            winDifferential--;
                        }
                        loserWinDifferentialMap.put(c.getName(), winDifferential);
                    }
                });

                if (game.getWinner().getCardsAcquiredByDeck().get(1) != null) {
                    game.getWinner().getCardsAcquiredByDeck().get(1).forEach(c -> {
                        Integer winsForCard = winnerFirstDeckWinsMap.get(c.getName());
                        if (winsForCard == null) {
                            winsForCard = 1;
                        } else {
                            winsForCard++;
                        }
                        winnerFirstDeckWinsMap.put(c.getName(), winsForCard);

                        Integer totalGamesForCard = winnerFirstDeckTotalGamesMap.get(c.getName());
                        if (totalGamesForCard == null) {
                            totalGamesForCard = 1;
                        } else {
                            totalGamesForCard++;
                        }
                        winnerFirstDeckTotalGamesMap.put(c.getName(), totalGamesForCard);
                    });
                }

                if (game.getLoser().getCardsAcquiredByDeck().get(1) != null) {
                    game.getLoser().getCardsAcquiredByDeck().get(1).forEach(c -> {
                        Integer totalGamesForCard = loserFirstDeckTotalGamesMap.get(c.getName());
                        if (totalGamesForCard == null) {
                            totalGamesForCard = 1;
                        } else {
                            totalGamesForCard++;
                        }
                        loserFirstDeckTotalGamesMap.put(c.getName(), totalGamesForCard);
                    });
                }

                if (game.getWinner().getCardsAcquiredByDeck().get(2) != null) {
                    game.getWinner().getCardsAcquiredByDeck().get(2).forEach(c -> {
                        Integer winsForCard = winnerSecondDeckWinsMap.get(c.getName());
                        if (winsForCard == null) {
                            winsForCard = 1;
                        } else {
                            winsForCard++;
                        }
                        winnerSecondDeckWinsMap.put(c.getName(), winsForCard);

                        Integer totalGamesForCard = winnerSecondDeckTotalGamesMap.get(c.getName());
                        if (totalGamesForCard == null) {
                            totalGamesForCard = 1;
                        } else {
                            totalGamesForCard++;
                        }
                        winnerSecondDeckTotalGamesMap.put(c.getName(), totalGamesForCard);
                    });
                }

                if (game.getLoser().getCardsAcquiredByDeck().get(2) != null) {
                    game.getLoser().getCardsAcquiredByDeck().get(2).forEach(c -> {
                        Integer totalGamesForCard = loserSecondDeckTotalGamesMap.get(c.getName());
                        if (totalGamesForCard == null) {
                            totalGamesForCard = 1;
                        } else {
                            totalGamesForCard++;
                        }
                        loserSecondDeckTotalGamesMap.put(c.getName(), totalGamesForCard);
                    });
                }
            }

            totalGamesCounted++;
            games.add(game);
            turnTotal += game.getTurn();
        }

        for (Game game : games) {
            Map<String, TreeMap<Integer, Integer>> authorityByPlayerByTurn = game.getAuthorityByPlayerByTurn();
            for (String playerName : authorityByPlayerByTurn.keySet()) {
                Map<Integer, Integer> authorityByTurn = authorityByPlayerByTurn.get(playerName);
                Map<Integer, Integer> averageAuthorityByTurn = averageAuthorityByPlayerByTurn.get(playerName);

                for (Integer turn : authorityByTurn.keySet()) {
                    Integer authority = averageAuthorityByTurn.get(turn);
                    if (authority == null) {
                        authority = 0;
                    }

                    authority += authorityByTurn.get(turn);

                    averageAuthorityByTurn.put(turn, authority);
                }
            }
        }

        for (String playerName : averageAuthorityByPlayerByTurn.keySet()) {
            Map<Integer, Integer> averageAuthorityByTurn = averageAuthorityByPlayerByTurn.get(playerName);
            for (Integer turn : averageAuthorityByTurn.keySet()) {
                Integer authority = averageAuthorityByTurn.get(turn);
                authority = authority / games.size();
                averageAuthorityByTurn.put(turn, authority);
            }
        }

        if (!simulatingBestBot && !simulatingBestCardToBuy) {
            playerTotalGamesByFirstDeckCard.keySet().forEach(cardName -> {
                Integer totalGamesForCard = playerTotalGamesByFirstDeckCard.get(cardName);
                Integer winsForCard = playerWinsByFirstDeckCard.get(cardName);
                if (winsForCard == null) {
                    playerWinPercentageByFirstDeckCard.put(cardName, 0f);
                } else {
                    playerWinPercentageByFirstDeckCard.put(cardName, ((float) winsForCard / totalGamesForCard) * 100);
                }
            });

            opponentTotalGamesByFirstDeckCard.keySet().forEach(cardName -> {
                Integer totalGamesForCard = opponentTotalGamesByFirstDeckCard.get(cardName);
                Integer winsForCard = opponentWinsByFirstDeckCard.get(cardName);
                if (winsForCard == null) {
                    opponentWinPercentageByFirstDeckCard.put(cardName, 0f);
                } else {
                    opponentWinPercentageByFirstDeckCard.put(cardName, ((float) winsForCard / totalGamesForCard) * 100);
                }
            });

            playerTotalGamesBySecondDeckCard.keySet().forEach(cardName -> {
                Integer totalGamesForCard = playerTotalGamesBySecondDeckCard.get(cardName);
                Integer winsForCard = playerWinsBySecondDeckCard.get(cardName);
                if (winsForCard == null) {
                    playerWinPercentageBySecondDeckCard.put(cardName, 0f);
                } else {
                    playerWinPercentageBySecondDeckCard.put(cardName, ((float) winsForCard / totalGamesForCard) * 100);
                }
            });

            opponentTotalGamesBySecondDeckCard.keySet().forEach(cardName -> {
                Integer totalGamesForCard = opponentTotalGamesBySecondDeckCard.get(cardName);
                Integer winsForCard = opponentWinsBySecondDeckCard.get(cardName);
                if (winsForCard == null) {
                    opponentWinPercentageBySecondDeckCard.put(cardName, 0f);
                } else {
                    opponentWinPercentageBySecondDeckCard.put(cardName, ((float) winsForCard / totalGamesForCard) * 100);
                }
            });

            playerTotalGamesByNumScoutsFirstTwoHands.keySet().forEach(scoutSplit -> {
                Integer totalGamesForScoutSplit = playerTotalGamesByNumScoutsFirstTwoHands.get(scoutSplit);
                Integer winsForScoutSplit = playerWinsByNumScoutsFirstTwoHands.get(scoutSplit);
                if (winsForScoutSplit == null) {
                    playerWinPercentageByNumScoutsFirstTwoHands.put(scoutSplit, 0f);
                } else {
                    playerWinPercentageByNumScoutsFirstTwoHands.put(scoutSplit, ((float) winsForScoutSplit / totalGamesForScoutSplit) * 100);
                }
            });

            opponentTotalGamesByNumScoutsFirstTwoHands.keySet().forEach(scoutSplit -> {
                Integer totalGamesForScoutSplit = opponentTotalGamesByNumScoutsFirstTwoHands.get(scoutSplit);
                Integer winsForScoutSplit = opponentWinsByNumScoutsFirstTwoHands.get(scoutSplit);
                if (winsForScoutSplit == null) {
                    opponentWinPercentageByNumScoutsFirstTwoHands.put(scoutSplit, 0f);
                } else {
                    opponentWinPercentageByNumScoutsFirstTwoHands.put(scoutSplit, ((float) winsForScoutSplit / totalGamesForScoutSplit) * 100);
                }
            });
        }

        DecimalFormat f = new DecimalFormat("##.00");

        float winPercentage;
        if (totalGamesCounted > 0) {
            winPercentage = ((float) wins / totalGamesCounted) * 100;
        } else {
            winPercentage = 0;
        }

        results.setTotalGamesCounted(totalGamesCounted);
        results.setWinPercentage(winPercentage);
        results.setAverageNumTurns(turnTotal / totalGamesCounted);

        for (String playerName : averageAuthorityByPlayerByTurn.keySet()) {
            Map<Integer, Integer> averageAuthorityByTurn = averageAuthorityByPlayerByTurn.get(playerName);
            if (playerName.equals(player.getPlayerName())) {
                results.setPlayerAverageAuthorityByTurn(averageAuthorityByTurn);
            } else {
                results.setOpponentAverageAuthorityByTurn(averageAuthorityByTurn);
            }
        }

        if (cardToBuyThisTurn == null) {
            results.setPlayerWinDifferentialByCardsAtEndOfGame(sortByValueDescending(playerWinDifferentialByCardsAtEndOfGame));
            results.setOpponentWinDifferentialByCardsAtEndOfGame(sortByValueDescending(opponentWinDifferentialByCardsAtEndOfGame));

            results.setPlayerWinPercentageByFirstDeckCard(sortByValueDescending(playerWinPercentageByFirstDeckCard));
            results.setOpponentWinPercentageByFirstDeckCard(sortByValueDescending(opponentWinPercentageByFirstDeckCard));

            results.setPlayerTotalGamesByFirstDeckCard(playerTotalGamesByFirstDeckCard);
            results.setOpponentTotalGamesByFirstDeckCard(opponentTotalGamesByFirstDeckCard);

            results.setPlayerWinPercentageBySecondDeckCard(sortByValueDescending(playerWinPercentageBySecondDeckCard));
            results.setOpponentWinPercentageBySecondDeckCard(sortByValueDescending(opponentWinPercentageBySecondDeckCard));

            results.setPlayerTotalGamesBySecondDeckCard(playerTotalGamesBySecondDeckCard);
            results.setOpponentTotalGamesBySecondDeckCard(opponentTotalGamesBySecondDeckCard);

            results.setPlayerWinPercentageByNumScoutsFirstTwoHands(sortByValueDescending(playerWinPercentageByNumScoutsFirstTwoHands));
            results.setOpponentWinPercentageByNumScoutsFirstTwoHands(sortByValueDescending(opponentWinPercentageByNumScoutsFirstTwoHands));

            results.setPlayerTotalGamesByNumScoutsFirstTwoHands(playerTotalGamesByNumScoutsFirstTwoHands);
            results.setOpponentTotalGamesByNumScoutsFirstTwoHands(opponentTotalGamesByNumScoutsFirstTwoHands);
        }

        return results;
    }

    private Game simulateGameToEnd(Game copiedGame, boolean createGameLog, Card cardToBuyThisTurn) {
        Game copiedGameCopy = copiedGame.copyGameForSimulation();
        Collections.shuffle(copiedGameCopy.getDeck());

        copiedGameCopy.setCreateGameLog(createGameLog);

        Player player = new StrategyBot(((StrategyBot) copiedGame.getCurrentPlayer()).getStrategy(), this, copiedGame.getCurrentPlayer(), copiedGameCopy);

        Player opponent = new StrategyBot(((StrategyBot) copiedGame.getCurrentPlayer().getOpponent()).getStrategy(), this, copiedGame.getCurrentPlayer().getOpponent(), copiedGameCopy);

        player.setPlayerName(player.getClass().getSimpleName() + "(Player)");

        opponent.setPlayerName(opponent.getClass().getSimpleName() + "(Opponent)");

        player.setCardToBuyThisTurn(cardToBuyThisTurn);

        List<Player> players = new ArrayList<>();

        player.setOpponent(opponent);
        opponent.setOpponent(player);

        if (copiedGame.getCurrentPlayer().isFirstPlayer()) {
            players.add(player);
            players.add(opponent);
        } else {
            players.add(opponent);
            players.add(player);
        }

        copiedGameCopy.setPlayers(players);

        if (cardToBuyThisTurn != null) {
            copiedGameCopy.setTrackAuthority(false);
        } else {
            copiedGameCopy.setupPlayerAuthorityMap();
        }

        copiedGameCopy.getCurrentPlayer().takeTurn();

        while (!copiedGameCopy.isGameOver()) {
            if (copiedGameCopy.getTurn() > 200) {
                //todo
                return null;
            } else {
                if (copiedGameCopy.isGameOver()) {
                    return copiedGameCopy;
                }
            }
        }

        return copiedGameCopy;
    }

    private <S, T extends Comparable> LinkedHashMap<S, T> sortByValueDescending(LinkedHashMap<S, T> map) {
        LinkedHashMap<S, T> result = new LinkedHashMap<>();
        Stream<Map.Entry<S, T>> st = map.entrySet().stream();

        st.sorted(Map.Entry.comparingByValue((o1, o2) -> o2.compareTo(o1)))
                .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }
}
