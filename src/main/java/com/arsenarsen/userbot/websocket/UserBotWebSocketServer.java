package com.arsenarsen.userbot.websocket;

/*
 *  This file is Copyright 2016 by MinorTom and licensed under the MIT License
 */

import com.arsenarsen.userbot.UserBot;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserBotWebSocketServer extends org.java_websocket.server.WebSocketServer {
    private static UserBotWebSocketServer instance;
    public static Logger LOGGER = UserBot.getLog(UserBotWebSocketServer.class);
    private UserBotWebSocket handler;
    private Set<WebSocket> connections = ConcurrentHashMap.newKeySet();

    public UserBotWebSocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        handler = new UserBotWebSocket();
        instance = this;
    }

    public static UserBotWebSocketServer getInstance() {
        return instance;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        UserBot.LOGGER.info("WebSocket Connection opened! Connection: {}! Handshake: {}!", conn, handshake);
        connections.add(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        UserBot.LOGGER.info("WebSocket Connection closed! Connection: {}! Reason: {}! Remote: {}!", conn, reason, remote);
        connections.remove(conn);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        UserBot.LOGGER.error("WebSocket error occured", ex);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            conn.send(UserBot.GSON.toJson(handler.handleEvent(UserBot.GSON.fromJson(message, WebSocketMessage.class))));
        } catch (Exception e) {
            UserBot.LOGGER.error("WebSocket Message Receive error", e);
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer blob) {
        onMessage(conn, new String(blob.array()));
    }

    public void send(WebSocketMessage message) {
        connections.forEach(conn -> conn.send(message.toString()));
    }

    public Set<WebSocket> getConnections() {
        return connections;
    }
}
