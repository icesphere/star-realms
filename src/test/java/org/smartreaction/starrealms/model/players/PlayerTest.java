package org.smartreaction.starrealms.model.players;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.smartreaction.starrealms.model.Game;
import org.smartreaction.starrealms.model.GameOptions;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.Faction;
import org.smartreaction.starrealms.model.cards.bases.Base;
import org.smartreaction.starrealms.model.cards.bases.blob.TradeWheel;
import org.smartreaction.starrealms.model.cards.bases.outposts.machinecult.BrainWorld;
import org.smartreaction.starrealms.model.cards.bases.outposts.machinecult.MachineBase;
import org.smartreaction.starrealms.model.cards.bases.outposts.starempire.RecyclingStation;
import org.smartreaction.starrealms.model.cards.bases.outposts.starempire.StarFortress;
import org.smartreaction.starrealms.model.cards.bases.tradefederation.BarterWorld;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.cards.ships.blob.BattlePod;
import org.smartreaction.starrealms.model.cards.ships.blob.BlobDestroyer;
import org.smartreaction.starrealms.model.cards.ships.blob.BlobFighter;
import org.smartreaction.starrealms.model.cards.ships.machinecult.TradeBot;
import org.smartreaction.starrealms.model.cards.ships.tradefederation.Freighter;
import org.smartreaction.starrealms.model.cards.ships.tradefederation.TradeEscort;
import org.smartreaction.starrealms.model.players.bots.StrategyBot;
import org.smartreaction.starrealms.model.players.bots.strategies.VelocityStrategy;
import org.smartreaction.starrealms.service.GameService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    GameService gameService = new GameService();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void copyFromPlayerForSimulation() {
        Player player1 = new StrategyBot(new VelocityStrategy(), gameService);

        Player player2 = new StrategyBot(new VelocityStrategy(), gameService);

        GameOptions gameOptions = new GameOptions();

        Game game = gameService.createGameForSimulation(gameOptions, player1, player2);

        TradeBot tradeBot = new TradeBot();
        player1.getHand().add(tradeBot);

        RecyclingStation recyclingStation = new RecyclingStation();
        player1.getDeck().add(recyclingStation);

        BarterWorld barterWorld = new BarterWorld();
        player1.getBases().add(barterWorld);

        BrainWorld brainWorld = new BrainWorld();
        player1.getDiscard().add(brainWorld);

        BlobFighter blobFighter = new BlobFighter();

        player1.getInPlay().add(blobFighter);
        player1.getInPlay().add(barterWorld);

        player1.getPlayed().add(blobFighter);

        player1.getFactionsPlayedThisTurn().add(Faction.BLOB);

        player1.turn = 5;

        player1.turns = 3;

        BlobDestroyer blobDestroyer = new BlobDestroyer();
        player2.getHand().add(blobDestroyer);

        TradeWheel tradeWheel = new TradeWheel();
        player2.getBases().add(tradeWheel);

        Explorer explorer1 = new Explorer();
        player2.getDiscard().add(explorer1);

        Freighter freighter = new Freighter();
        player2.getDeck().add(freighter);

        player2.turn = 4;

        player2.turns = 2;

        player2.getCardsInHandBeforeShuffle().add(blobDestroyer);

        Player player1Copy = new StrategyBot(new VelocityStrategy(), gameService);

        Game gameCopy = game.copyGameForSimulation();

        Game gameCopyOfCopy = gameCopy.copyGameForSimulation();

        player1Copy.setGame(gameCopy);

        player1Copy.copyFromPlayerForSimulation(player1, true, false);

        Player player2Copy = new StrategyBot(new VelocityStrategy(), gameService);

        player2Copy.setGame(gameCopy);

        player2Copy.copyFromPlayerForSimulation(player2, false, false);

        Player player1CopyOfCopy = new StrategyBot(new VelocityStrategy(), gameService);

        player1CopyOfCopy.getBases().add(new StarFortress());

        player1CopyOfCopy.setGame(gameCopyOfCopy);

        player1CopyOfCopy.getCardsInHandBeforeShuffle().add(new BattlePod());

        player1CopyOfCopy.copyFromPlayerForSimulation(player1Copy, true, true);

        Player player2CopyOfCopy = new StrategyBot(new VelocityStrategy(), gameService);

        player2CopyOfCopy.setGame(gameCopyOfCopy);

        player2CopyOfCopy.getBases().add(new MachineBase());

        player2CopyOfCopy.getCardsInHandBeforeShuffle().add(new TradeEscort());

        player2CopyOfCopy.copyFromPlayerForSimulation(player2Copy, false, true);

        assertTrue(game.getCurrentPlayer().equals(player1));

        assertEquals(5, player1Copy.getTurn());
        assertEquals(5, player1CopyOfCopy.getTurn());
        assertEquals(3, player1Copy.getTurns());
        assertEquals(3, player1CopyOfCopy.getTurns());

        assertEquals(4, player2Copy.getTurn());
        assertEquals(4, player2CopyOfCopy.getTurn());
        assertEquals(2, player2Copy.getTurns());
        assertEquals(2, player2CopyOfCopy.getTurns());

        assertEquals(8, player1Copy.getDeck().size());
        assertEquals(8, player1CopyOfCopy.getDeck().size());
        assertTrue(hasCard(player1Copy.getDeck(), RecyclingStation.class));
        assertTrue(hasCard(player1CopyOfCopy.getDeck(), RecyclingStation.class));

        Card recyclingStationCopy = findCard(player1Copy.getDeck(), RecyclingStation.class);
        assertFalse(player1.getDeck().contains(recyclingStationCopy));
        assertTrue(player1Copy.getDeck().contains(recyclingStationCopy));
        assertTrue(player1CopyOfCopy.getDeck().contains(recyclingStationCopy));

        assertEquals(6, player2Copy.getDeck().size());
        assertEquals(6, player2CopyOfCopy.getDeck().size());
        assertTrue(hasCard(player2Copy.getHandAndDeck(), Freighter.class));
        assertTrue(hasCard(player2CopyOfCopy.getHandAndDeck(), Freighter.class));

        Card freighterCopy = findCard(player2Copy.getHandAndDeck(), Freighter.class);
        assertFalse(player2.getHandAndDeck().contains(freighterCopy));
        assertTrue(player2Copy.getHandAndDeck().contains(freighterCopy));
        assertTrue(player2CopyOfCopy.getHandAndDeck().contains(freighterCopy));

        assertEquals(4, player1Copy.getHand().size());
        assertEquals(4, player1CopyOfCopy.getHand().size());
        assertTrue(hasCard(player1Copy.getHand(), TradeBot.class));
        assertTrue(hasCard(player1CopyOfCopy.getHand(), TradeBot.class));

        assertTrue(player1.getCardsInHandBeforeShuffle().isEmpty());
        assertTrue(player1Copy.getCardsInHandBeforeShuffle().isEmpty());
        assertTrue(player1CopyOfCopy.getCardsInHandBeforeShuffle().isEmpty());

        assertEquals(1, player2Copy.getCardsInHandBeforeShuffle().size());
        assertEquals(1, player2CopyOfCopy.getCardsInHandBeforeShuffle().size());
        assertTrue(hasCard(player2Copy.getCardsInHandBeforeShuffle(), BlobDestroyer.class));
        assertTrue(hasCard(player2CopyOfCopy.getCardsInHandBeforeShuffle(), BlobDestroyer.class));

        assertEquals(6, player2Copy.getHand().size());
        assertEquals(6, player2CopyOfCopy.getHand().size());
        assertTrue(hasCard(player2Copy.getHand(), BlobDestroyer.class));
        assertTrue(hasCard(player2CopyOfCopy.getHand(), BlobDestroyer.class));

        Card tradeBotCopy = findCard(player1Copy.getHand(), TradeBot.class);
        assertFalse(player1.getHand().contains(tradeBotCopy));
        assertTrue(player1Copy.getHand().contains(tradeBotCopy));
        assertTrue(player1CopyOfCopy.getHand().contains(tradeBotCopy));

        Card blobDestroyerCopy = findCard(player2Copy.getHand(), BlobDestroyer.class);
        assertFalse(player2.getHand().contains(blobDestroyerCopy));
        assertTrue(player2Copy.getHand().contains(blobDestroyerCopy));
        assertTrue(player2CopyOfCopy.getHand().contains(blobDestroyerCopy));

        assertEquals(1, player1Copy.getDiscard().size());
        assertEquals(1, player1CopyOfCopy.getDiscard().size());
        assertTrue(hasCard(player1Copy.getDiscard(), BrainWorld.class));
        assertTrue(hasCard(player1CopyOfCopy.getDiscard(), BrainWorld.class));

        assertEquals(1, player2Copy.getDiscard().size());
        assertEquals(1, player2CopyOfCopy.getDiscard().size());
        assertTrue(hasCard(player2Copy.getDiscard(), Explorer.class));
        assertTrue(hasCard(player2CopyOfCopy.getDiscard(), Explorer.class));

        Card brainWorldCopy = findCard(player1Copy.getDiscard(), BrainWorld.class);
        assertFalse(player1.getDiscard().contains(brainWorldCopy));
        assertTrue(player1Copy.getDiscard().contains(brainWorldCopy));
        assertTrue(player1CopyOfCopy.getDiscard().contains(brainWorldCopy));

        Card explorerCopy = findCard(player2Copy.getDiscard(), Explorer.class);
        assertFalse(player2.getDiscard().contains(explorerCopy));
        assertTrue(player2Copy.getDiscard().contains(explorerCopy));
        assertTrue(player2CopyOfCopy.getDiscard().contains(explorerCopy));

        assertEquals(1, player1Copy.getBases().size());
        assertEquals(1, player1CopyOfCopy.getBases().size());
        assertTrue(hasCard(player1Copy.getBases(), BarterWorld.class));
        assertTrue(hasCard(player1CopyOfCopy.getBases(), BarterWorld.class));

        Base barterWorldCopy = (Base) findCard(player1Copy.getBases(), BarterWorld.class);
        assertFalse(player1.getBases().contains(barterWorldCopy));
        assertTrue(player1Copy.getBases().contains(barterWorldCopy));
        assertTrue(player1CopyOfCopy.getBases().contains(barterWorldCopy));

        assertEquals(1, player2Copy.getBases().size());
        assertEquals(1, player2CopyOfCopy.getBases().size());
        assertTrue(hasCard(player2Copy.getBases(), TradeWheel.class));
        assertTrue(hasCard(player2CopyOfCopy.getBases(), TradeWheel.class));

        Base tradeWheelCopy = (Base) findCard(player2Copy.getBases(), TradeWheel.class);
        assertFalse(player2.getBases().contains(tradeWheelCopy));
        assertTrue(player2Copy.getBases().contains(tradeWheelCopy));
        assertTrue(player2CopyOfCopy.getBases().contains(tradeWheelCopy));

        assertEquals(2, player1Copy.getInPlay().size());
        assertEquals(2, player1CopyOfCopy.getInPlay().size());
        assertTrue(hasCard(player1Copy.getInPlay(), BlobFighter.class));
        assertTrue(hasCard(player1Copy.getInPlay(), BarterWorld.class));

        assertEquals(0, player2Copy.getInPlay().size());
        assertEquals(0, player2CopyOfCopy.getInPlay().size());

        assertEquals(1, player1Copy.getPlayed().size());
        assertEquals(1, player1CopyOfCopy.getPlayed().size());
        assertTrue(hasCard(player1Copy.getPlayed(), BlobFighter.class));
        assertTrue(hasCard(player1CopyOfCopy.getPlayed(), BlobFighter.class));

        assertTrue(player1.factionPlayedThisTurn(Faction.BLOB));
        assertTrue(player1Copy.factionPlayedThisTurn(Faction.BLOB));
        assertTrue(player1CopyOfCopy.factionPlayedThisTurn(Faction.BLOB));

        assertEquals(0, player2Copy.getPlayed().size());
        assertEquals(0, player2CopyOfCopy.getPlayed().size());

        assertTrue(player1.getHand().contains(tradeBot));
        assertFalse(player1Copy.getHand().contains(tradeBot));
        assertFalse(player1CopyOfCopy.getHand().contains(tradeBot));

        assertTrue(player2.getHand().contains(blobDestroyer));
        assertFalse(player2Copy.getHand().contains(blobDestroyer));
        assertFalse(player2CopyOfCopy.getHand().contains(blobDestroyer));

        assertTrue(player1.getDeck().contains(recyclingStation));
        assertFalse(player1Copy.getDeck().contains(recyclingStation));
        assertFalse(player1CopyOfCopy.getDeck().contains(recyclingStation));

        assertTrue(player2.getDeck().contains(freighter));
        assertFalse(player2Copy.getDeck().contains(freighter));
        assertFalse(player2CopyOfCopy.getDeck().contains(freighter));

        assertTrue(player1.getDiscard().contains(brainWorld));
        assertFalse(player1Copy.getDiscard().contains(brainWorld));
        assertFalse(player1CopyOfCopy.getDiscard().contains(brainWorld));

        assertTrue(player2.getDiscard().contains(explorer1));
        assertFalse(player2Copy.getDiscard().contains(explorer1));
        assertFalse(player2CopyOfCopy.getDiscard().contains(explorer1));

        assertTrue(player1.getBases().contains(barterWorld));
        assertFalse(player1Copy.getBases().contains(barterWorld));
        assertFalse(player1CopyOfCopy.getBases().contains(barterWorld));

        assertTrue(player2.getBases().contains(tradeWheel));
        assertFalse(player2Copy.getBases().contains(tradeWheel));
        assertFalse(player2CopyOfCopy.getBases().contains(tradeWheel));

        assertTrue(player1.getInPlay().contains(barterWorld));
        assertFalse(player1Copy.getInPlay().contains(barterWorld));
        assertFalse(player1CopyOfCopy.getInPlay().contains(barterWorld));

        assertTrue(player1.getInPlay().contains(blobFighter));
        assertFalse(player1Copy.getInPlay().contains(blobFighter));
        assertFalse(player1CopyOfCopy.getInPlay().contains(blobFighter));

        assertTrue(player1.getPlayed().contains(blobFighter));
        assertFalse(player1Copy.getPlayed().contains(blobFighter));
        assertFalse(player1CopyOfCopy.getPlayed().contains(blobFighter));
    }

    public boolean hasCard(List<? extends Card> cards, Class<? extends Card> cardClass) {
        return cards.stream().anyMatch(c -> c.getClass().equals(cardClass));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Card findCard(List<? extends Card> cards, Class<? extends Card> cardClass) {
        return cards.stream().filter(c -> c.getClass().equals(cardClass)).findFirst().get();
    }

}