package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Facepalm implements Command {

    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        msg.editMessage("*facepalm*").queue();
    }

    @Override
    public String getName() {
        return "facepalm";
    }

    @Override
    public String getUsage() {
        return "When someone fails, you need to facepalm.";
    }
}
