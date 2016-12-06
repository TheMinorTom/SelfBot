package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class Quote implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if (args.length >= 2) {
            if (args[0].matches("\\d+")) {
                channel.getHistoryAround(args[0], 100).queue(messageHistory -> {
                    Message msg2 = messageHistory.getMessageById(args[0]);
                    if (msg2 != null) {
                        User auth = msg2.getAuthor();
                        String cnt = msg.getRawContent();
                        cnt = cnt.substring(UserBot.getInstance().getConfig().getProperty("prefix").length() + getName().length() + 1);
                        cnt = cnt.substring(args[0].length());
                        EmbedBuilder builder = new EmbedBuilder().setAuthor(auth.getName() + '#' + auth.getDiscriminator(), null, DiscordUtils.gerAvatar(auth))
                                .setDescription(msg2.getRawContent())
                                .setColor(new Color((int) (Math.random() * 0x1000000)))
                                .setFooter(msg2.getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME), null)
                                .addField("Channel: ", "<#" + channel.getId() + ">", true);
                        for (Message.Attachment attachment : msg2.getAttachments()) {
                            builder.setImage(attachment.getUrl());
                        }
                        msg.editMessage(new MessageBuilder()
                                .setEmbed(builder.build())
                                .append(cnt)
                                .build()).queue();
                    }
                });
            }
        }
    }

    @Override
    public String getName() {
        return "quote";
    }

    @Override
    public String getUsage() {
        return "Quotes someones message. Usage: quote MESSAGE_ID Your comment; To get the message ID, enable developers mode and right click the message -> Copy ID.";
    }
}
