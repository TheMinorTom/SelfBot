package com.arsenarsen.userbot.websocket;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.websocket.handler.TestHandler;

import java.util.HashMap;
import java.util.Map;

/*
 *  This file is Copyright 2016 by MinorTom and licensed under the MIT License
 */
public class UserBotWebSocket {

    public static WebSocketMessage handleEvent(WebSocketMessage msg){
        for (WebSocketMessageHandler h : handlers.values()){
            if (h.getName().equalsIgnoreCase(msg.handler)){
                UserBot.LOGGER.info("Dispatching webhook with handler " + msg.handler);
                    try{
                        return h.dispatch(msg);
                    } catch (Exception e){
                        UserBot.LOGGER.error("Error Dispatching WebSocket request", e);
                    }
                break;
            }
        }
        UserBot.LOGGER.error("Unknown WebSocket message received: " + msg.handler);
        WebSocketMessage retmsg = new WebSocketMessage();
        retmsg.action = "RESPONSE";
        retmsg.error = "UNKNOWN_HANDLER";
        retmsg.message = "";
        return retmsg;
    }

    public static void register(){
        UserBotWebSocket.registerHandler(new TestHandler());
    }

    public static Map<String,WebSocketMessageHandler> handlers = new HashMap<>();

    public static boolean registerHandler(WebSocketMessageHandler handler) {
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

