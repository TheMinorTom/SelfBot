package com.arsenarsen.userbot;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.arsenarsen.userbot.guiutils.ExceptionBox;

public class LogIntercepter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!LauncherEntry.nogui &&event.getThrowableProxy() != null && event.getThrowableProxy() instanceof ThrowableProxy) {
            @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
            Throwable throwable = ((ThrowableProxy) event.getThrowableProxy()).getThrowable();
            if (throwable != null) {
                new ExceptionBox(throwable, "Cought an error in runtime!").show();
            }
        }
        return FilterReply.NEUTRAL;
    }
}
