package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Sigh implements Command {

    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        msg.editMessage("**sigh**").queue();
    }

    @Override
    public String getName() {
        return "sigh";
    }

    @Override
    public String getUsage() {
        return "Sigh when someone says something stupid.";
    }
}
