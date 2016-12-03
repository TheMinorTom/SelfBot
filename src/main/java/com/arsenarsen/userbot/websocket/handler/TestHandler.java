package com.arsenarsen.userbot.websocket.handler;

import com.arsenarsen.userbot.websocket.WebSocketMessage;
import com.arsenarsen.userbot.websocket.WebSocketMessageHandler;

/*
 *  This file is Copyright 2016 by MinorTom and licensed under the MIT License
 */
public class TestHandler implements WebSocketMessageHandler{
    @Override
    public WebSocketMessage dispatch(WebSocketMessage msg) {
        WebSocketMessage retur = new WebSocketMessage();
        retur.action = "MESSAGE_RETURN";
        retur.error = "OK";
        retur.message = "Hi";
        return retur;
    }

    @Override
    public String getName() {
        return "Test";
    }
}
