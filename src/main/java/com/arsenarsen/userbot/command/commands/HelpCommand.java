package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.util.Comparator;

public class HelpCommand implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        EmbedBuilder builder = new EmbedBuilder().setAuthor("UserBot v" + UserBot.VERSION, "https://github.com/ArsenArsen/SelfBot", null);
        builder.setTitle("\tBy Arsen#3291");
        builder.setColor(new Color((int) (0x1000000 * Math.random())));
        UserBot.getInstance().getDispatcher().getCommands().stream().sorted(Comparator.comparing(Command::getName))
                .forEach(command -> builder.addField(command.getName(), "```fix\n" + command.getUsage() + "```", true));
        msg.editMessage(new MessageBuilder().setEmbed(builder.build()).build()).queue();
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
