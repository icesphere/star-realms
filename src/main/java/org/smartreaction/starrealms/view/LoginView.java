package org.smartreaction.starrealms.view;

import org.smartreaction.starrealms.service.LoggedInUsers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class LoginView implements Serializable {
    @ManagedProperty(value = "#{userSession}")
    UserSession userSession;

    @EJB
    LoggedInUsers loggedInUsers;

    private String username;

    private boolean showUsernameError;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String loginAsGuest() {
        if (userSession.loginAsGuest(username)) {
            showUsernameError = false;
            return "lobby.xhtml?faces-redirect=true";
        } else {
            showUsernameError = true;
            return null;
        }
    }

    public boolean isShowUsernameError() {
        return showUsernameError;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
