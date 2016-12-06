package com.arsenarsen.userbot.websockets.handler.handlers;

import ch.qos.logback.classic.Level;
import com.arsenarsen.userbot.websockets.handler.EventHandler;
import com.arsenarsen.userbot.websockets.handler.Message;

import static ch.qos.logback.classic.Level.*;
import static com.arsenarsen.userbot.LauncherEntry.LOGGER;

public class LogHandler implements EventHandler {
    @Override
    public String getName() {
        return "LOG";
    }

    @Override
    public void handle(Message msg) {
        String[] spl = msg.getMessage().split("\u200B", 2);
        String lvl = spl[0];
        String message = spl[1];
        Level level = Level.toLevel(lvl, Level.OFF);
        if (level == ALL || level == INFO) {
            LOGGER.info(String.valueOf(message));
        } else if (level == ERROR) {
            LOGGER.error(String.valueOf(message));
        } else if (level == WARN) {
            LOGGER.warn(String.valueOf(message));
        } else if (level == DEBUG) {
            LOGGER.debug(String.valueOf(message));
        } else if (level == TRACE) {
            LOGGER.trace(String.valueOf(message));
        }
    }
}
