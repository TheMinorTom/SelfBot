package com.arsenarsen.userbot.websocket;

/*
 *  This file is Copyright 2016 by MinorTom and licensed under the MIT License
 */

import com.arsenarsen.userbot.UserBot;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.FrameBuilder;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

public class UserBotWebSocketServer extends org.java_websocket.server.WebSocketServer {
    public static UserBotWebSocketServer instance;

    public UserBotWebSocketServer(int port, Draft d) throws UnknownHostException {
        super(new InetSocketAddress(port), Collections.singletonList(d));
        UserBotWebSocket.register();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        UserBot.LOGGER.info("WebSocket Connection opened");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        UserBot.LOGGER.info("WebSocket Connection closed");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        UserBot.LOGGER.error("WebSocket error occured", ex);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            conn.send(UserBot.GSON.toJson(UserBotWebSocket.handleEvent(UserBot.GSON.fromJson(message, WebSocketMessage.class))));
        } catch (Throwable e) {
            UserBot.LOGGER.error("WebSocket Message Receive error", e);
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer blob) {
        onMessage(conn, new String(blob.array()));
    }

    @Override
    public void onWebsocketMessageFragment(WebSocket conn, Framedata frame) {
        FrameBuilder builder = (FrameBuilder) frame;
        builder.setTransferemasked(false);
        // But what does this mean?
    }
}
