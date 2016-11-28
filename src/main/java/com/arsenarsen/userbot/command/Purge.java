package com.arsenarsen.userbot.command;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Purge implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if(args.length == 1 && args[0].matches("\\d+")){
            channel.getHistory().getCachedHistory().stream()
                    .limit(Integer.parseInt(args[0]))
                    .forEach(message -> message.deleteMessage().queue());
        }
    }

    @Override
    public String getName() {
        return "purge";
    }
}
