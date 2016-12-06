package com.arsenarsen.userbot.websockets.handler;

import com.arsenarsen.userbot.Launcher;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Handler {
    private Set<EventHandler> handlers = ConcurrentHashMap.newKeySet();

    public boolean registerHandler(EventHandler handler) {
        return handlers.stream()
                .map(EventHandler::getName)
                .filter(name -> name.equalsIgnoreCase(handler.getName()))
                .count() <= 0 && handlers.add(handler);
    }

    public void handle(String message) {
        try {
            Message msg = Message.fromJson(message);
            handlers.stream().filter(h -> h.getName().equalsIgnoreCase(msg.getHandler())).forEach(s -> s.handle(msg));
        } catch (Exception e){
            Launcher.LOGGER.error("WebSocket handler error!",e);
        }
    }
}
