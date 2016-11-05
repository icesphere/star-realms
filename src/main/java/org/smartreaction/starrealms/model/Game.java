package org.smartreaction.starrealms.model;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.events.Event;
import org.smartreaction.starrealms.model.cards.missions.Mission;
import org.smartreaction.starrealms.model.cards.missions.united.*;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.players.HumanPlayer;
import org.smartreaction.starrealms.model.players.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class Game
{
    private String gameId;

    public static final boolean SHOW_GAME_LOG = false;

    private int turn;

    private List<Player> players;

    private List<Card> deck = new ArrayList<>();

    private List<Card> tradeRow = new ArrayList<>();

    private List<Card> tradeRowCardsScrapped = new ArrayList<>();

    private int currentPlayerIndex;

    private boolean gameOver;

    private boolean createGameLog = true;

    private StringBuilder gameLog = new StringBuilder();

    private StringBuilder currentTurnLog = new StringBuilder();

    private Map<String, TreeMap<Integer, Integer>> authorityByPlayerByTurn = new HashMap<>();

    private boolean trackAuthority = true;

    private Set<CardSet> cardSets = new HashSet<>();

    private Player quitGamePlayer;

    private List<ChatMessage> chatMessages = new ArrayList<>();

    private boolean simulation;

    private List<Mission> allMissions = null;

    private List<String> recentTurnLogs = new ArrayList<>();

    private boolean includeSimulationInfo;

    private boolean timedOut;

    StringBuilder lastTurnSimulationInfoLog = new StringBuilder();
    StringBuilder currentTurnSimulationInfoLog = new StringBuilder();

    public Game() {
        gameId = UUID.randomUUID().toString();
    }

    public Game copyGameForSimulation() {
        Game game = new Game();
        game.setCurrentPlayerIndex(currentPlayerIndex);
        game.getTradeRow().addAll(copyCards(tradeRow));
        game.setTradeRowCardsScrapped(new ArrayList<>(tradeRowCardsScrapped));
        game.getDeck().addAll(copyCards(deck));
        game.setTurn(turn);
        game.setCreateGameLog(false);
        game.setTrackAuthority(false);
        game.setSimulation(true);
        game.setCardSets(new HashSet<>(cardSets));
        return game;
    }

    private List<? extends Card> copyCards(List<? extends Card> cardsToCopy) {
        return cardsToCopy
                .stream()
                .map(Card::copyCardForSimulation)
                .collect(toList());
    }

    public int getTurn()
    {
        return turn;
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<Player> players)
    {
        this.players = players;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getTradeRow() {
        return tradeRow;
    }

    public void setTradeRow(List<Card> tradeRow) {
        this.tradeRow = tradeRow;
    }

    public void setTradeRowCardsScrapped(List<Card> tradeRowCardsScrapped) {
        this.tradeRowCardsScrapped = tradeRowCardsScrapped;
    }

    public Card getExplorer() {
        return new Explorer();
    }

    public List<Card> getTradeRowCardsScrapped() {
        return tradeRowCardsScrapped;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void addCardToTradeRow() {
        addCardsToTradeRow(1);
    }

    public void addCardsToTradeRow(int cards) {
        for (int i = 0; i < cards; i++) {
            if (!deck.isEmpty()) {
                addCardToTradeRow(deck.remove(0));
            }
        }
        gameLog("Trade Row: " + getCardsAsString(tradeRow));
    }

    public void addCardToTradeRow(Card card) {
        gameLog("Added " + card.getName() + " to the trade row");
        tradeRow.add(card);
        getCurrentPlayer().getCurrentTurnSummary().getCardsAddedToTradeRow().add(card);
        if (card instanceof Event) {
            ((Event) card).eventTriggered(getCurrentPlayer());
        }
    }

    public void startGame() {
        currentPlayerIndex = 0;
        turn = 1;
        setupPlayerAuthorityMap();

        startTurnInNewThreadIfComputerVsHuman();
    }

    private void startTurnInNewThreadIfComputerVsHuman() {
        if (getCurrentPlayer().isBot() && getCurrentPlayer().getOpponent() instanceof HumanPlayer) {
            HumanPlayer humanPlayer = (HumanPlayer) getCurrentPlayer().getOpponent();
            humanPlayer.setWaitingForComputer(true);
            new Thread(() -> {
                getCurrentPlayer().startTurn();
            }).start();
        } else {
            getCurrentPlayer().startTurn();
        }
    }

    public void setupPlayerAuthorityMap() {
        if (trackAuthority) {
            for (Player player : players) {
                TreeMap<Integer, Integer> playerAuthority = new TreeMap<>();
                authorityByPlayerByTurn.put(player.getSimulationPlayerId(), playerAuthority);
                playerAuthority.put(player.getTurns(), player.getAuthority());
            }
        }
    }

    public String getCardsAsString(List cards) {
        String cardString = "";
        boolean first = true;
        for (Object card : cards) {
            if (!first) {
                cardString += ", ";
            } else {
                first = false;
            }
            cardString += ((Card) card).getName();
        }
        return cardString;
    }

    public void turnEnded() {
        gameLog("End of turn " + turn);
        for (Player player : players) {
            gameLog(player.getPlayerName() + "'s authority: " + player.getAuthority());
            if (trackAuthority) {
                TreeMap<Integer, Integer> playerAuthority = authorityByPlayerByTurn.get(player.getSimulationPlayerId());
                playerAuthority.put(player.getTurns(), player.getAuthority());
            }
        }

        for (Player player : players) {
            boolean completedAllMissions = allMissionsCompleted(player);
            if (player.getAuthority() <= 0 || completedAllMissions) {
                gameOver();
                return;
            }
        }

        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }

        turn++;

        recentTurnLogs.add(currentTurnLog.toString());

        currentTurnLog.setLength(0);

        if (includeSimulationInfo) {
            lastTurnSimulationInfoLog = new StringBuilder(currentTurnSimulationInfoLog.toString());
            currentTurnSimulationInfoLog.setLength(0);
        }

        if (recentTurnLogs.size() > 1) {
            recentTurnLogs.remove(0);
        }

        if (!isGameOver() && !simulation) {
            startTurnInNewThreadIfComputerVsHuman();
        }
    }

    public boolean allMissionsCompleted(Player player) {
        return usingMissions() && player.getUnClaimedMissions().isEmpty();
    }

    private void gameOver() {
        gameOver = true;
        gameLog("-----------------------------");
        gameLog("Game over");
        gameLog("Turns: " + turn);
        for (Player player : players) {
            player.setWaitingForComputer(false);
            String playerName = player.getPlayerName();
            gameLog(playerName + "'s authority: " + player.getAuthority());
        }
        for (Player player : players) {
            gameLog("----");
            String playerName = player.getPlayerName();
            gameLog(playerName + "'s cards: ");
            player.getAllCards().forEach(c -> gameLog(c.getName()));
            if (usingMissions()) {
                gameLog(playerName + "'s missions:");
                player.getMissions().forEach(m -> gameLog(m.getName()));
            }
        }

        if (!simulation) {
            writeGameLog();
        }
    }

    public File getGameLogFile() {
        File userDirectory = FileUtils.getUserDirectory();
        File gameLogDirectory = new File(userDirectory, "starrealmsgamelogs");

        String gameLogFileName = "game_";

        if (quitGamePlayer != null) {
            gameLogFileName += quitGamePlayer.getPlayerName() + "_quit_";
        }

        if (timedOut) {
            gameLogFileName += "timeout_";
        }

        gameLogFileName += getWinner().getInfoForGameLogName() + "_over_" + getLoser().getInfoForGameLogName() + "_" + gameId;

        return new File(gameLogDirectory, gameLogFileName);
    }

    private void writeGameLog() {
        try {
            File gameLogFile = getGameLogFile();
            //gameLog.append("Game log file: ").append(gameLogFile.getAbsolutePath());
            FileUtils.writeStringToFile(gameLogFile,
                    gameLog.toString().replaceAll("<br/>", "\n").replaceAll("<b>", "").replaceAll("</b>", ""),
                    "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Player getWinner() {
        if (usingMissions()) {
            for (Player player : players) {
                if (allMissionsCompleted(player)) {
                    return player;
                }
            }
        }
        return players.stream().max((p1, p2) -> Integer.compare(p1.getAuthority(), p2.getAuthority())).get();
    }

    public Player getLoser() {
        if (usingMissions()) {
            for (Player player : players) {
                if (allMissionsCompleted(player)) {
                    return player.getOpponent();
                }
            }
        }
        return players.stream().min((p1, p2) -> Integer.compare(p1.getAuthority(), p2.getAuthority())).get();
    }

    public void gameLog(String log) {
        gameLog(log, false);
    }

    private void gameLog(String log, boolean simulationInfo) {
        if (SHOW_GAME_LOG) {
            System.out.println(log);
        }
        if (createGameLog) {
            if (!simulationInfo || includeSimulationInfo) {
                gameLog.append(log).append("<br/>");
            }
            if (simulationInfo && includeSimulationInfo) {
                currentTurnSimulationInfoLog.append(log).append("<br/>");
            }
            currentTurnLog.append(log).append("<br/>");
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public Map<String, TreeMap<Integer, Integer>> getAuthorityByPlayerByTurn() {
        return authorityByPlayerByTurn;
    }

    public boolean isTrackAuthority() {
        return trackAuthority;
    }

    public void setTrackAuthority(boolean trackAuthority) {
        this.trackAuthority = trackAuthority;
    }

    public boolean isCreateGameLog() {
        return createGameLog;
    }

    public void setCreateGameLog(boolean createGameLog) {
        this.createGameLog = createGameLog;
    }

    public StringBuilder getGameLog() {
        return gameLog;
    }

    public void setGameLog(StringBuilder gameLog) {
        this.gameLog = gameLog;
    }

    public void scrapCardFromTradeRow(Card card) {
        gameLog("Scrapped " + card.getName() + " from trade row");
        tradeRow.remove(card);
        tradeRowCardsScrapped.add(card);
        addCardToTradeRow();
    }

    public void scrapAllCardsFromTradeRow() {
        List<Card> cardsInTradeRow = new ArrayList<>(tradeRow);
        for (Card card : cardsInTradeRow) {
            scrapCardFromTradeRow(card);
        }
    }

    public Set<CardSet> getCardSets() {
        return cardSets;
    }

    public void setCardSets(Set<CardSet> cardSets) {
        this.cardSets = cardSets;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void quitGame(Player player) {
        quitGamePlayer = player;
        gameLog(player.getPlayerName() + " quit the game");
        gameOver();
    }

    public Player getQuitGamePlayer() {
        return quitGamePlayer;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public List<Mission> getAllMissions() {
        if (allMissions == null) {
            allMissions = new ArrayList<>();
            
            allMissions.add(new Ally());
            allMissions.add(new Armada());
            allMissions.add(new Colonize());
            allMissions.add(new Convert());
            allMissions.add(new Defend());
            allMissions.add(new Diversify());
            allMissions.add(new Dominate());
            allMissions.add(new Exterminate());
            allMissions.add(new Influence());
            allMissions.add(new Monopolize());
            allMissions.add(new Rule());
            allMissions.add(new Unite());
        }

        return allMissions;
    }

    public String getRecentTurnsLog() {
        return currentTurnLog + StringUtils.join(recentTurnLogs, "");
    }

    public boolean usingMissions() {
        return cardSets.contains(CardSet.UNITED_MISSIONS);
    }

    public void setIncludeSimulationInfo(boolean includeSimulationInfo) {
        this.includeSimulationInfo = includeSimulationInfo;
    }

    public String getLastTurnSimulationInfoLog() {
        return lastTurnSimulationInfoLog.toString();
    }

    public void addSimulationLog(String log) {
        gameLog(log, true);
    }

    public void gameTimedOut() {
        gameLog("Game timed out");
        timedOut = true;
        writeGameLog();
    }
}
