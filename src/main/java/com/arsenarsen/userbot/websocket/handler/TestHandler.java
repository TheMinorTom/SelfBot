package com.arsenarsen.userbot.websocket.handler;

import com.arsenarsen.userbot.websocket.WebSocketMessage;
import com.arsenarsen.userbot.websocket.WebSocketMessageHandler;

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
public class TestHandler implements WebSocketMessageHandler{
    @Override
    public WebSocketMessage dispatch(WebSocketMessage msg) {
        WebSocketMessage retur = new WebSocketMessage();
        retur.action = "MESSAGE_RETURN";
        retur.error = "OK";
        retur.message = "Hi";
        return retur;
    }

    @Override
    public String getName() {
        return "Test";
    }
}
