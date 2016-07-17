package org.smartreaction.starrealms.model.message;

import org.primefaces.push.Decoder;

public class GameMessageDecoder implements Decoder<String, GameMessage> {
    @Override
    public GameMessage decode(String s) {
        String[] userAndMessage = s.split(":");
        if (userAndMessage.length >= 2) {
            return new GameMessage().setUser(userAndMessage[0]).setMessage(userAndMessage[1]);
        }
        else {
            return new GameMessage(s);
        }
    }
}
