package org.smartreaction.starrealms.service;

import com.google.gson.Gson;
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
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.cards.missions.united.*;
import org.smartreaction.starrealms.model.cards.scenarios.*;
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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;

@Stateless
public class GameService {
    private EventBus eventBus;

    private final static String GAME_CHANNEL = "/game/";

    private final Object matchUserLock = new Object();

    Gson gson = new Gson();

    @EJB
    LoggedInUsers loggedInUsers;

    @EJB
    SimulatorService simulatorService;

    public Game createGame(User user1, User user2, GameOptions gameOptions) {
        simulatorService.gameService = this;

        Game game = new Game();

        HumanPlayer player1 = new HumanPlayer(user1);

        Player player2;

        if (gameOptions.isPlayAgainstComputer()) {
            player2 = new SimulatorBot(this, simulatorService);
            user2 = new User();
            user2.setUsername(player2.getPlayerName());
            game.setIncludeSimulationInfo(gameOptions.isIncludeSimulationInfo());
        } else {
            player2 = new HumanPlayer(user2);
        }

        List<Player> players = new ArrayList<>(2);

        if (user1.getGameOptions().getPlayerOrder().equals("second")
                || user2.getGameOptions().getPlayerOrder().equals("first")) {
            players.add(player2);
            players.add(player1);
        } else {
            players.add(player1);
            players.add(player2);
        }

        user1.setCurrentPlayer(player1);
        user1.setCurrentGame(game);

        user2.setCurrentPlayer(player2);
        user2.setCurrentGame(game);

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        players.forEach(p -> p.setGame(game));

        if (user1.getGameOptions().getPlayerOrder().equals("random")
                && user2.getGameOptions().getPlayerOrder().equals("random")) {
            Collections.shuffle(players);
        }

        game.setPlayers(players);

        game.getCurrentPlayer().setFirstPlayer(true);

        game.gameLog("<br/>** Starting Game **");
        game.gameLog("Game options: " + gameOptions.toString());
        game.gameLog("Player 1: " + players.get(0).getPlayerName() + " - Player 2: " + players.get(1).getPlayerName());

        setupCards(game, gameOptions);

        game.setupGame();

        game.startGame();

        return game;
    }

    public Game createGameForSimulation(GameOptions gameOptions, Player player1, Player player2) {
        Game game = new Game();

        List<Player> players = new ArrayList<>(2);

        players.add(player1);
        players.add(player2);

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        players.forEach(p -> p.setGame(game));

        game.setPlayers(players);

        game.getCurrentPlayer().setFirstPlayer(true);

        game.gameLog("<br/>** Starting Game **");
        game.gameLog("Game options: " + gameOptions.toString());
        game.gameLog("Player 1: " + players.get(0).getPlayerName() + " - Player 2: " + players.get(1).getPlayerName());

        setupCards(game, gameOptions);

        game.setupGame();

        return game;
    }

    public void setupCards(Game game, GameOptions gameOptions) {
        for (Player player : game.getPlayers()) {
            setupPlayerCards(player);
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
        if (gameOptions.isIncludeUnitedAssault()) {
            deck.addAll(getUnitedAssault());
            game.getCardSets().add(CardSet.UNITED_ASSAULT);
        }
        if (gameOptions.isIncludeUnitedCommand()) {
            deck.addAll(getUnitedCommand());
            game.getCardSets().add(CardSet.UNITED_COMMAND);
        }
        if (gameOptions.isIncludeUnitedHeroes()) {
            deck.addAll(getUnitedHeroes());
            game.getCardSets().add(CardSet.UNITED_HEROES);
        }

        if (gameOptions.isIncludeUnitedMissions()) {
            addMissions(game);
            game.getCardSets().add(CardSet.UNITED_MISSIONS);
        }

        if (gameOptions.isIncludeGambits()) {
            addGambits(game);
            game.getCardSets().add(CardSet.GAMBITS);
        }

        if (gameOptions.isIncludeScenarios()) {
            if (gameOptions.getScenario() != null) {
                game.setScenario(getScenarioFromName(gameOptions.getScenario()));
            }

            if (game.getScenario() == null) {
                game.setScenario(getRandomScenario());
            }

            game.getCardSets().add(CardSet.SCENARIOS);

            if (game.getScenario() instanceof StartOfGameScenario) {
                ((StartOfGameScenario) game.getScenario()).applyScenarioToGame(game);
            }
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
            if (tradeRow.size() < game.getTradeRowSize()) {
                game.addCardsToTradeRow(game.getTradeRowSize() - tradeRow.size());
            }
        } else {
            game.addCardsToTradeRow(game.getTradeRowSize());
        }

    }

    private Scenario getRandomScenario() {
        List<Scenario> scenarios = getScenarios();

        return scenarios.get(new Random().nextInt(scenarios.size()));
    }

    private List<Scenario> getScenarios() {
        List<Scenario> scenarios = new ArrayList<>();

        scenarios.add(new BorderSkirmish());
        //scenarios.add(new BuyersMarket());
        //scenarios.add(new CommitmentToTheCause());
        //scenarios.add(new EarlyRecruitment());
        scenarios.add(new EmergencyRepairs());
        scenarios.add(new EntrenchedLoyalties());
        scenarios.add(new FlareMining());
        scenarios.add(new ProlongedConflict());
        scenarios.add(new ReadyReserves());
        scenarios.add(new RecruitingDrive());
        scenarios.add(new RushedDefenses());
        scenarios.add(new RuthlessEfficiency());
        scenarios.add(new TotalWar());
        scenarios.add(new WarpgateNexus());

        return scenarios;
    }

    public void setupPlayerCards(Player player) {
        for (int i = 0; i < 8; i++) {
            player.addCardToDeck(new Scout());
        }

        player.addCardToDeck(new Viper());
        player.addCardToDeck(new Viper());

        player.setup();
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

    public List<Card> getUnitedAssault() {
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

    public List<Card> getUnitedCommand() {
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

    public List<Mission> getUnitedMissions() {
        List<Mission> missions = new ArrayList<>();

        missions.add(new Ally());
        missions.add(new Armada());
        missions.add(new Colonize());
        missions.add(new Convert());
        missions.add(new Defend());
        missions.add(new Diversify());
        missions.add(new Dominate());
        missions.add(new Exterminate());
        missions.add(new Influence());
        missions.add(new Monopolize());
        missions.add(new Rule());
        missions.add(new Unite());

        return missions;
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

    private void addMissions(Game game) {
        List<Mission> missions = getUnitedMissions();

        Collections.shuffle(missions);

        List<Mission> missionsForFirstPlayer = missions.subList(0, 3);
        game.getPlayers().get(0).setMissions(missionsForFirstPlayer);

        List<Mission> missionsForSecondPlayer = missions.subList(3, 6);
        game.getPlayers().get(1).setMissions(missionsForSecondPlayer);
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

    public Scenario getScenarioFromName(String name) {
        name = name.replaceAll("\\s", "").toLowerCase();
        name = name.replaceAll("'", "");

        switch (name) {
            case "borderskirmish":
                return new BorderSkirmish();
            case "buyersmarket":
                return new BuyersMarket();
            case "commitmenttothecause":
                return new CommitmentToTheCause();
            case "earlyrecruitment":
                return new EarlyRecruitment();
            case "emergencyrepairs":
                return new EmergencyRepairs();
            case "entrenchedloyalties":
                return new EntrenchedLoyalties();
            case "flaremining":
                return new FlareMining();
            case "prolongeconflict":
                return new ProlongedConflict();
            case "readyreserves":
                return new ReadyReserves();
            case "recruitingdrive":
                return new RecruitingDrive();
            case "rusheddefenses":
                return new RushedDefenses();
            case "ruthlessefficiency":
                return new RuthlessEfficiency();
            case "totalwar":
                return new TotalWar();
            case "warpgatenexus":
                return new WarpgateNexus();
            default:
                return null;
        }
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

            case "admras":
            case "admiralrasmussen":
                return new AdmiralRasmussen();

            case "bloove":
            case "bloboverlord":
                return new BlobOverlord();

            case "ceotor":
            case "ceotorres":
                return new CeoTorres();

            case "cuncap":
            case "cunningcaptain":
                return new CunningCaptain();

            case "higprilyl":
            case "highpriestlyle":
                return new HighPriestLyle();

            case "rampil":
            case "rampilot":
                return new RamPilot();

            case "speopsdir":
            case "specialopsdirector":
                return new SpecialOpsDirector();

            case "warelder":
            case "wareld":
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

    public Mission getMissionByName(String missionName) {
        if (missionName == null) {
            return null;
        }

        missionName = missionName.replaceAll("\\s", "").toLowerCase();
        missionName = missionName.replaceAll("'", "");

        switch (missionName) {
            case "ally":
                return new Ally();
            case "armada":
                return new Armada();
            case "colonize":
                return new Colonize();
            case "convert":
                return new Convert();
            case "defend":
                return new Defend();
            case "diversify":
                return new Diversify();
            case "dominate":
                return new Dominate();
            case "exterminate":
                return new Exterminate();
            case "influence":
                return new Influence();
            case "monopolize":
                return new Monopolize();
            case "rule":
                return new Rule();
            case "unite":
                return new Unite();
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

        refreshLobby(user.getUsername());
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

    public void saveGameOptions(User user) {
        sendLobbyMessage(user.getUsername(), user.getUsername(), "save_game_options;" + gson.toJson(user.getGameOptions()));
        refreshLobby(user.getUsername());
    }

    public void sendGameMessage(String sender, String recipient, String channel, String message) {
        getEventBus().publish(GAME_CHANNEL + channel + "/" + recipient, sender + ";" + message);
    }

    EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = EventBusFactory.getDefault().eventBus();
        }
        return eventBus;
    }

    public void startInviteMatch(User user) {
        synchronized (matchUserLock) {
            user.getGameOptions().setPlayAgainstComputer(false);
            user.getInvitee().getGameOptions().setPlayAgainstComputer(false);
            createGame(user, user.getInvitee(), user.getInvitee().getGameOptions());
            sendLobbyMessage(user.getUsername(), user.getInvitee().getUsername(), "game_started");
            user.getInvitee().setInvitee(null);
            user.getInvitee().setInviteeRequested(null);
            user.setInvitee(null);
            user.setInviteeRequested(null);
        }
    }

    public void inviteMatchUser(User user, User opponent) {
        synchronized (matchUserLock) {
            user.setAutoMatch(false);
            opponent.setAutoMatch(false);

            opponent.setInvitee(user);
            user.setInviteeRequested(opponent);
        }
    }

    public void cancelInviteMatch(User user) {
        try {
            user.getInviteeRequested().setInvitee(null);
            user.getInviteeRequested().setInviteeRequested(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            user.getInvitee().setInvitee(null);
            user.getInvitee().setInviteeRequested(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setInvitee(null);
        user.setInviteeRequested(null);
    }

    public void cancelAutoMatch(User user) {
        user.setAutoMatch(false);
    }
}
