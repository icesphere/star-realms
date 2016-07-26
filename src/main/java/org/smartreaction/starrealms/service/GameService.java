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
import org.smartreaction.starrealms.model.cards.bases.starempire.FleetHQ;
import org.smartreaction.starrealms.model.cards.bases.starempire.OrbitalPlatform;
import org.smartreaction.starrealms.model.cards.bases.starempire.StarbaseOmega;
import org.smartreaction.starrealms.model.cards.bases.tradefederation.*;
import org.smartreaction.starrealms.model.cards.events.*;
import org.smartreaction.starrealms.model.cards.gambits.*;
import org.smartreaction.starrealms.model.cards.heroes.*;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.cards.ships.MercCruiser;
import org.smartreaction.starrealms.model.cards.ships.Scout;
import org.smartreaction.starrealms.model.cards.ships.Viper;
import org.smartreaction.starrealms.model.cards.ships.blob.*;
import org.smartreaction.starrealms.model.cards.ships.machinecult.*;
import org.smartreaction.starrealms.model.cards.ships.starempire.*;
import org.smartreaction.starrealms.model.cards.ships.tradefederation.*;
import org.smartreaction.starrealms.model.players.HumanPlayer;
import org.smartreaction.starrealms.model.players.Player;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        HumanPlayer player2 = new HumanPlayer(user2);

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

        if (gameOptions.determineIncludeBaseSet()) {
            deck.addAll(getBaseSetDeck());
            game.getCardSets().add(CardSet.CORE);
        }
        if (gameOptions.determineIncludeColonyWars()) {
            deck.addAll(getColonyWarsDeck());
            game.getCardSets().add(CardSet.COLONY_WARS);
        }
        if (gameOptions.determineIncludeYearOnePromos()) {
            deck.addAll(getYear1PromoCards());
            game.getCardSets().add(CardSet.PROMO_YEAR_1);
        }
        if (gameOptions.determineIncludeCrisisBasesAndBattleships()) {
            deck.addAll(getCrisisBasesAndBattleships());
            game.getCardSets().add(CardSet.CRISIS_BASES_AND_BATTLESHIPS);
        }
        if (gameOptions.determineIncludeCrisisEvents()) {
            deck.addAll(getCrisisEvents());
            game.getCardSets().add(CardSet.CRISIS_EVENTS);
        }
        if (gameOptions.determineIncludeCrisisFleetsAndFortresses()) {
            deck.addAll(getCrisisFleetsAndFortresses());
            game.getCardSets().add(CardSet.CRISIS_FLEETS_AND_FORTRESSES);
        }
        if (gameOptions.determineIncludeCrisisHeroes()) {
            deck.addAll(getCrisisHeroes());
            game.getCardSets().add(CardSet.CRISIS_HEROES);
        }

        if (gameOptions.determineIncludeGambits()) {
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

    public void autoMatchUser(User user, GameOptions gameOptions) {
        synchronized (matchUserLock) {
            if (user.getCurrentGame() != null) {
                return;
            }
            List<User> users = loggedInUsers.getUsersWaitingForAutoMatch();
            if (!users.isEmpty()) {
                User opponent = users.get(0);
                opponent.setAutoMatch(false);
                user.setAutoMatch(false);
                createGame(user, opponent, gameOptions);
                sendLobbyMessage(user.getUsername(), opponent.getUsername(), "game_started");
            } else {
                user.setAutoMatch(true);
            }
        }
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
}
