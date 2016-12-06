package com.arsenarsen.userbot.websocket.handler;

import com.arsenarsen.userbot.websocket.WebSocketMessage;
import com.arsenarsen.userbot.websocket.WebSocketMessageHandler;

/*
 *  This file is Copyright 2016 by MinorTom and licensed under the MIT License
 */
public class TestHandler implements WebSocketMessageHandler{
    @Override
    public WebSocketMessage dispatch(WebSocketMessage msg) {
        return new WebSocketMessage("Hi", null, WebSocketMessage.Action.RESPONSE, this.getName());
    }

    @Override
    public String getName() {
        return "Test";
    }
}
