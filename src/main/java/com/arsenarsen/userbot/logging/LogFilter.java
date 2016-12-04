package com.arsenarsen.userbot.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.arsenarsen.userbot.websocket.UserBotWebSocketServer;
import com.arsenarsen.userbot.websocket.messages.LogMessage;

public class LogFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if(UserBotWebSocketServer.getInstance() != null){
            UserBotWebSocketServer.getInstance().send(new LogMessage(event));
        }
        return FilterReply.NEUTRAL;
    }
}
