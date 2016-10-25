package org.smartreaction.starrealms.view;

import org.smartreaction.starrealms.model.User;
import org.smartreaction.starrealms.service.GameService;
import org.smartreaction.starrealms.service.LoggedInUsers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class UserSession implements Serializable {
    @EJB
    LoggedInUsers loggedInUsers;

    @EJB
    GameService gameService;

    private boolean loggedIn;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean loginAsGuest(String username, String betaCode) {
        if (!loggedInUsers.isUsernameInUse(username)) {
            user = new User();
            user.setUsername(username);
            user.setBetaCode(betaCode);
            loggedIn = true;
            loggedInUsers.getUsers().add(user);
            gameService.refreshLobby(username);
            return true;
        }

        return false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String logout() {
        loggedInUsers.getUsers().remove(user);
        gameService.refreshLobby(user.getUsername());
        user = null;
        loggedIn = false;

        return "login.xhtml?faces-redirect=true";
    }
}
