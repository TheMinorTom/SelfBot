package com.arsenarsen.userbot.websockets.handler;

public interface EventHandler {
    String getName();
    void handle(Message msg);
}
