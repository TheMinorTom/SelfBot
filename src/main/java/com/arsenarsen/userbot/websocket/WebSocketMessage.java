package com.arsenarsen.userbot.websocket;

import com.arsenarsen.userbot.UserBot;

/*
 *  This file is Copyright 2016 by MinorTom and licensed under the MIT License
 */
public class WebSocketMessage {

    // Suppressed due to these being converted to JSON
    @SuppressWarnings("FieldCanBeLocal")
    private String handler = "";
    @SuppressWarnings("FieldCanBeLocal")
    private String action = "";
    @SuppressWarnings("FieldCanBeLocal")
    private String error = "";
    @SuppressWarnings("FieldCanBeLocal")
    private String message = "";

    public WebSocketMessage(String message, String error, Action action, WebSocketMessageHandler handler) {
        this.message = message;
        this.error = error;
        this.action = action.name();
        this.handler = handler == null ? null : handler.getName();
    }

    @Override
    public String toString() {
        return UserBot.GSON.toJson(this);
    }

    public String getHandler() {
        return handler;
    }

    public Action getAction() {
        return Action.get(action);
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public enum Action {
        RESPONSE,
        NOTIFY, REQUEST;

        public static Action get(String action) {
            try {
                return valueOf(action.toUpperCase());
            } catch (Exception e) {
                return null;
            }
        }
    }
}
