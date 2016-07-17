package org.smartreaction.starrealms.model.message;

import org.primefaces.json.JSONObject;
import org.primefaces.push.Encoder;

public class GameMessageEncoder implements Encoder<GameMessage, String> {
    @Override
    public String encode(GameMessage message) {
        return new JSONObject(message).toString();
    }
}
