package com.arsenarsen.userbot.websocket;

import com.arsenarsen.userbot.UserBot;
import com.google.gson.JsonArray;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 *  This file is Copyright 2016 by MinorTom and licensed under the MIT License
 */
public class UserBotWebSocket {

    private Map<String, WebSocketMessageHandler> handlers = new ConcurrentHashMap<>();

    public WebSocketMessage handleEvent(WebSocketMessage msg) {
        for (WebSocketMessageHandler h : handlers.values()) {
            if (h.getName().equalsIgnoreCase(msg.getHandler())) {
                UserBot.LOGGER.info("Dispatching webhook with handler " + msg.getHandler());
                try {
                    return h.dispatch(msg);
                } catch (Exception e) {
                    UserBot.LOGGER.error("Error Dispatching WebSocket request", e);
                }
                break;
            }
        }
        UserBot.LOGGER.error("Unknown WebSocket message received: " + msg.getHandler());
        JsonArray avail = new JsonArray();
        handlers.keySet().forEach(avail::add);
        return new WebSocketMessage("{\"available\": " + avail + "}", "UNKNOWN_HANDLER", WebSocketMessage.Action.RESPONSE, null);
    }


    public boolean registerHandler(WebSocketMessageHandler handler) {
        if (handler.getName().contains(" ")) {
            throw new IllegalArgumentException("Name must not have spaces!");
        }
        if (handlers.containsKey(handler.getName().toLowerCase())) {
            return false;
        }
        handlers.put(handler.getName().toLowerCase(), handler);
        return true;
    }
}

