package org.smartreaction.starrealms.view;

import org.apache.commons.lang3.StringUtils;
import org.ocpsoft.prettytime.PrettyTime;
import org.smartreaction.starrealms.model.ChatMessage;
import org.smartreaction.starrealms.model.User;
import org.smartreaction.starrealms.service.GameService;
import org.smartreaction.starrealms.service.LoggedInUsers;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class LobbyView implements Serializable {
    @ManagedProperty(value="#{userSession}")
    UserSession userSession;

    @EJB
    GameService gameService;

    @EJB
    LoggedInUsers loggedInUsers;

    String chatMessage = "";

    public List<User> getUsers() {
        return loggedInUsers.getActiveUsers();
    }

    public String startAutoMatch() {
        gameService.autoMatchUser(userSession.getUser());
        gameService.refreshLobby(userSession.getUser().getUsername());
        if (userSession.getUser().getCurrentGame() != null) {
            return "game.xhtml?faces-redirect=true";
        }
        return null;
    }

    public String getUserStatus(User user) {
        if (user.getCurrentGame() != null) {
            return "(playing game with " + user.getCurrentPlayer().getOpponent().getPlayerName() + ")";
        }

        PrettyTime p = new PrettyTime();
        String lastActivity = p.format(Date.from(user.getLastActivity()));

        if (user.isAutoMatch()) {
            return "(waiting for auto match: " + lastActivity + ")";
        }

        return "(last activty: " + lastActivity + ")";
    }

    public List<ChatMessage> getChatMessages() {
        return loggedInUsers.getChatMessages();
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void sendChatMessage() {
        if (!StringUtils.isEmpty(chatMessage)) {
            loggedInUsers.getChatMessages().add(new ChatMessage(userSession.getUser().getUsername(), chatMessage));
            chatMessage = "";
            gameService.sendLobbyMessageToAll(userSession.getUser().getUsername(), "refresh_chat");
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
