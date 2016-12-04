package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Ping implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        long a = System.currentTimeMillis();
        msg.editMessage("Pong!")
                .queue(s -> s.editMessage("Pong! `" + (System.currentTimeMillis() - a) + "ms`").queue());
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getUsage() {
        return "pong";
    }
}
