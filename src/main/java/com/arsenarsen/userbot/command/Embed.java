package com.arsenarsen.userbot.command;

import com.arsenarsen.userbot.UserBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class Embed implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        msg.deleteMessage().queue();
        if(args.length == 0)
            return;
        String cnt = msg.getRawContent();
        cnt = cnt.substring(getName().length());
        cnt = cnt.substring(UserBot.getInstance().getConfig().getProperty("prefix").length());
        channel.sendMessage(new MessageBuilder().setEmbed(new EmbedBuilder().setDescription(cnt).build()).build()).queue();
    }

    @Override
    public String getName() {
        return "embed";
    }
}
