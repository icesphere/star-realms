package org.smartreaction.starrealms.service;

import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.*;
import org.smartreaction.starrealms.model.message.GameMessage;
import org.smartreaction.starrealms.model.message.GameMessageDecoder;
import org.smartreaction.starrealms.model.message.GameMessageEncoder;

import javax.inject.Inject;
import javax.servlet.ServletContext;

@PushEndpoint("/game/{game}/{user}")
@Singleton
public class GameMessageResource {
    @PathParam
    private String game;

    @PathParam
    private String username;

    @Inject
    private ServletContext ctx;

    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
    }

    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
        System.out.println("websocket closed");
    }

    @OnMessage(decoders = {GameMessageDecoder.class}, encoders = {GameMessageEncoder.class})
    public GameMessage onMessage(GameMessage message) {
        return message;
    }
}
