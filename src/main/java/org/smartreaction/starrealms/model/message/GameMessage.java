package org.smartreaction.starrealms.model.message;

public class GameMessage {
    public GameMessage() {
    }

    public GameMessage(String user) {
        this.user = user;
    }

    public GameMessage(String user, String message) {
        this.user = user;
        this.message = message;
    }

    private String user;

    private String message;

    private String data;

    public String getUser() {
        return user;
    }

    public GameMessage setUser(String user) {
        this.user = user;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public GameMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getData() {
        return data;
    }

    public GameMessage setData(String data) {
        this.data = data;
        return this;
    }
}
