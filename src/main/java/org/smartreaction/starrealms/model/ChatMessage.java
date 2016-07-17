package org.smartreaction.starrealms.model;

import java.util.Date;

public class ChatMessage {
    private String username;

    private String message;

    private Date messageDate;

    public ChatMessage(String username, String message) {
        this.username = username;
        this.message = message;
        this.messageDate = new Date();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }
}
