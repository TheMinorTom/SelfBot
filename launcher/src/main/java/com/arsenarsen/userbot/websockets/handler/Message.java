package com.arsenarsen.userbot.websockets.handler;

import com.arsenarsen.userbot.LauncherEntry;

public class Message {

    // Suppressed due to these being converted to JSON
    @SuppressWarnings("FieldCanBeLocal")
    private String handler = "";
    @SuppressWarnings("FieldCanBeLocal")
    private Action action;
    @SuppressWarnings("FieldCanBeLocal")
    private String error = "";
    @SuppressWarnings("FieldCanBeLocal")
    private String message = "";

    public Message(String message, String error, Action action, EventHandler handler) {
        this.message = message;
        this.error = error;
        this.action = action;
        this.handler = handler == null ? null : handler.getName();
    }

    @Override
    public String toString() {
        return LauncherEntry.GSON.toJson(this);
    }

    public String getHandler() {
        return handler;
    }

    public Action getAction() {
        return action;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public static Message fromJson(String json){
        return LauncherEntry.GSON.fromJson(json, Message.class);
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
