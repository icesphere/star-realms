package org.smartreaction.starrealms.model.players;

import org.smartreaction.starrealms.model.User;

public class HumanPlayer extends Player {
    private User user;

    public HumanPlayer(User user) {
        this.user = user;
        playerName = user.getUsername();
    }

    public User getUser() {
        return user;
    }

    @Override
    public void takeTurn() {

    }
}
