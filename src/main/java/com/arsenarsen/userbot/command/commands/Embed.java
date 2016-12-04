package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;

public class Embed implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if(args.length == 0)
            return;
        String cnt = msg.getRawContent();
        cnt = cnt.substring(getName().length());
        cnt = cnt.substring(UserBot.getInstance().getConfig().getProperty("prefix").length());
        msg.editMessage(new MessageBuilder().setEmbed(new EmbedBuilder()
                .setColor(new Color((int) (Math.random() * 0x1000000)))
                .setAuthor(msg.getAuthor().getName() + '#' + msg.getAuthor().getDiscriminator(), null, DiscordUtils.gerAvatar(msg.getAuthor()))
                .setDescription(cnt).build()).build()).queue();
    }

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getUsage() {
        return "Makes a fancy embed instead of a normal message :o";
    }
}
