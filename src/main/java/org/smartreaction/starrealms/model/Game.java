package org.smartreaction.starrealms.model;

import org.smartreaction.starrealms.model.cards.Card;
import org.smartreaction.starrealms.model.cards.events.Event;
import org.smartreaction.starrealms.model.cards.ships.Explorer;
import org.smartreaction.starrealms.model.players.Player;

import java.util.*;

public class Game
{
    private String gameId;

    public static final boolean SHOW_GAME_LOG = false;

    private int turn;

    private List<Player> players;

    private List<Card> deck;

    private List<Card> tradeRow = new ArrayList<>();

    private List<Card> tradeRowCardsScrapped = new ArrayList<>();

    private int currentPlayerIndex;

    private boolean gameOver;

    private boolean createGameLog = true;

    private StringBuilder gameLog = new StringBuilder();

    private Map<String, TreeMap<Integer, Integer>> authorityByPlayerByTurn = new HashMap<>();

    private boolean trackAuthority = true;

    private Set<CardSet> cardSets = new HashSet<>();

    private Player quitGamePlayer;

    private List<ChatMessage> chatMessages = new ArrayList<>();

    public Game() {
        gameId = UUID.randomUUID().toString();
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
        tradeRow.add(card);
        if (card instanceof Event) {
            ((Event) card).eventTriggered(getCurrentPlayer());
        }
    }

    public void startGame() {
        currentPlayerIndex = 0;
        turn = 1;
        setupPlayerAuthorityMap();
        getCurrentPlayer().startTurn();
    }

    public void setupPlayerAuthorityMap() {
        if (trackAuthority) {
            for (Player player : players) {
                TreeMap<Integer, Integer> playerAuthority = new TreeMap<>();
                authorityByPlayerByTurn.put(player.getPlayerName(), playerAuthority);
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
                TreeMap<Integer, Integer> playerAuthority = authorityByPlayerByTurn.get(player.getPlayerName());
                playerAuthority.put(player.getTurns(), player.getAuthority());
            }
        }

        for (Player player : players) {
            if (player.getAuthority() <= 0) {
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

        if (!isGameOver()) {
            getCurrentPlayer().startTurn();
        }
    }

    private void gameOver() {
        gameOver = true;
        gameLog("-----------------------------");
        gameLog("Game over");
        gameLog("Turns: " + turn);
        for (Player player : players) {
            String playerName = player.getPlayerName();
            gameLog(playerName + "'s authority: " + player.getAuthority());
        }
        for (Player player : players) {
            gameLog("----");
            String playerName = player.getPlayerName();
            gameLog(playerName + "'s cards: ");
            player.getAllCards().forEach(c -> gameLog(c.getName()));
        }
    }

    public Player getWinner() {
        return players.stream().max((p1, p2) -> Integer.compare(p1.getAuthority(), p2.getAuthority())).get();
    }

    public Player getLoser() {
        return players.stream().min((p1, p2) -> Integer.compare(p1.getAuthority(), p2.getAuthority())).get();
    }

    public void gameLog(String log) {
        if (SHOW_GAME_LOG) {
            System.out.println(log);
        }
        if (createGameLog) {
            gameLog.insert(0, log + "<br/>");
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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void quitGame(Player player) {
        quitGamePlayer = player;
        gameOver = true;
    }

    public Player getQuitGamePlayer() {
        return quitGamePlayer;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }
}
