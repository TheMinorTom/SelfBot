package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.Messages;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Comparator;

public class HelpCommand implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        StringBuilder help = new StringBuilder().append("***UserBot v").append(UserBot.VERSION)
                .append("***\nKnown commands: \n```fix\n");
        UserBot.getInstance().getDispatcher().getCommands().stream()
                .sorted((Comparator.comparing(Command::getName)))
                .forEach(cmd -> help.append(cmd.getName()).append(" - ").append(cmd.getUsage()).append('\n'));
        Messages.edit(msg, help.append("\n```").append("*Made by Arsen#3291* <https://github.com/ArsenArsen/SelfBot>").toString());
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getUsage() {
        return "Lists commands. Simple?";
    }
}
