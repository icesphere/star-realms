package org.smartreaction.starrealms.model;

import org.smartreaction.starrealms.model.players.Player;

import java.time.Instant;
import java.util.Objects;

public class User {
    private String username;

    private String betaCode;

    private Game currentGame;

    private Player currentPlayer;

    private User invitee;
    private User inviteeRequested;

    private boolean autoMatch;

    private Instant lastActivity = Instant.now();

    private GameOptions gameOptions = new GameOptions();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBetaCode() {
        return betaCode;
    }

    public void setBetaCode(String betaCode) {
        this.betaCode = betaCode;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public User getInvitee() {
        return invitee;
    }

    public User getInviteeRequested() {
        return inviteeRequested;
    }

    public void setInviteeRequested(User inviteeRequested) {
        this.inviteeRequested = inviteeRequested;
    }

    public void setInvitee(User invitee) {
        this.invitee = invitee;
    }

    public boolean isAutoMatch() {
        return autoMatch;
    }

    public void setAutoMatch(boolean autoMatch) {
        this.autoMatch = autoMatch;
    }

    public Instant getLastActivity() {
        return lastActivity;
    }

    public void updateLastActivity() {
        lastActivity = Instant.now();
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }

    public void setGameOptions(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.username, other.username);
    }
}
