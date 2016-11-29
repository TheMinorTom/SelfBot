package com.arsenarsen.userbot.command;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

/**
 * ayy command
 * <br>
 * Created by Arsen on 21.9.16..
 */
public interface Command {

    void dispatch(String[] args, MessageChannel channel, Message msg);

    String getName();

    String getUsage();
}
