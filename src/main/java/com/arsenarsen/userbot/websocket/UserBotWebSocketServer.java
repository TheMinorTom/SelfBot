package com.arsenarsen.userbot.websocket;

/*
 *  UserBot Copyright (C) 2016 ArsenArsen, MinorTom
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

public class UserBotWebSocketServer extends org.java_websocket.server.WebSocketServer{
    public static UserBotWebSocketServer instance;

    public UserBotWebSocketServer(int port , Draft d ) throws UnknownHostException {
        super( new InetSocketAddress( port ), Collections.singletonList( d ) );
    }

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        UserBot.LOGGER.info("WebSocket Connection opened");
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        UserBot.LOGGER.info("WebSocket Connection closed");
    }

    @Override
    public void onError( WebSocket conn, Exception ex ) {
        UserBot.LOGGER.error("WebSocket error occured", ex);
    }

    @Override
    public void onMessage( WebSocket conn, String message ) {
        conn.send( message );
    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer blob ) {
        conn.send("Whats an ByteBuffer?");
    }

    @Override
    public void onWebsocketMessageFragment( WebSocket conn, Framedata frame ) {
        FrameBuilder builder = (FrameBuilder) frame;
        builder.setTransferemasked( false );
        conn.sendFrame( frame );
    }
}
