package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Purge implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if (args.length == 1 && args[0].matches("\\d+")) {
            channel.getHistory().retrievePast(Integer.parseInt(args[0])).queue(messages -> messages
                    .stream()
                    .filter(msg2 -> msg2.getAuthor().equals(UserBot.getInstance().getJda().getSelfUser()))
                    .forEach(message -> message.deleteMessage().queue()));
        }
    }

    @Override
    public String getName() {
        return "purge";
    }

    @Override
    public String getUsage() {
        return "Removes all your messages from the last X messages. Usage: purge X";
    }
}
