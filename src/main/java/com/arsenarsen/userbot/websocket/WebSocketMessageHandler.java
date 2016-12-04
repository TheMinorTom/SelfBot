package com.arsenarsen.userbot.websocket;

/*
 *  This file is Copyright 2016 by MinorTom and licensed under the MIT License
 */
public interface WebSocketMessageHandler {
    WebSocketMessage dispatch(WebSocketMessage msg);

    String getName();
}
