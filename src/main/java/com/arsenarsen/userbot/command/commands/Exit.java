package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Exit implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        System.exit(1);
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getUsage() {
        return "Exits the bot";
    }
}
