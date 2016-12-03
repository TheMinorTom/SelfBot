package com.arsenarsen.userbot.websocket;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.websocket.handler.TestHandler;

import java.util.HashMap;
import java.util.Map;

/*
 *  UserBot Copyright (C) 2016 MinorTom
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

