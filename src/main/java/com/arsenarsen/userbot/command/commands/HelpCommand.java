package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.Messages;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class HelpCommand implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        StringBuilder help = new StringBuilder().append("Known commands: \n```fix\n");
        UserBot.getInstance().getDispatcher().getCommands().stream()
                .sorted(String::compareTo)
                .forEach(cmd -> help.append(cmd).append('\n'));
        Messages.edit(msg, help.append("\n```").toString());
    }

    @Override
    public String getName() {
        return "help";
    }
}
