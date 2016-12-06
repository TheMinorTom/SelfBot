package com.arsenarsen.userbot.websocket.messages;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import com.arsenarsen.userbot.websocket.WebSocketMessage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

public class LogMessage extends WebSocketMessage {
    public LogMessage(ILoggingEvent event) {
        super(event.getLevel().levelStr + "\u200B" + renderEvent(event), null, Action.NOTIFY, "LOG");
    }

    private static String renderEvent(ILoggingEvent event) {
        Throwable throwable = null;
        if (event.getThrowableProxy() != null && event.getThrowableProxy() instanceof ThrowableProxy) {
            throwable = ((ThrowableProxy) event.getThrowableProxy()).getThrowable();
        }
        if (throwable != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            String trace = sw.toString();
            pw.close();
            return formatted(event) + trace;
        } else return formatted(event);
    }

    private static String formatted(ILoggingEvent event) {
        return "[" +
                new SimpleDateFormat("yyyy.MM.dd HH:mm").format(event.getTimeStamp()) +
                " " +
                (event.getLoggerName().contains(".") ? event.getLoggerName().substring(event.getLoggerName().lastIndexOf('.'))
                        : event.getLoggerName()) +
                " " + Thread.currentThread().getName() + ' ' + event.getLevel() + "] " +
                event.getFormattedMessage() + '\n';
    }
}
