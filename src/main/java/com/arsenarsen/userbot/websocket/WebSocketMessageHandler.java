package com.arsenarsen.userbot.websocket;

public interface WebSocketMessageHandler{
    WebSocketMessage dispatch(WebSocketMessage msg);

    String getName();
}
