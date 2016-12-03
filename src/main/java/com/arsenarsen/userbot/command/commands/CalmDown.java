package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jsoup.Jsoup;

import java.io.IOException;

public class CalmDown implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            String calmnessSource =
                    Jsoup.connect("http://random.cat/").get().body().select("#cat").first().attr("abs:src");
            msg.editMessage("*Calm...*\n" + calmnessSource).queue();
        } catch (IOException e) {
            msg.editMessage("You cannot be calmed D:").queue();
        }
    }

    @Override
    public String getName() {
        return "calmdown";
    }

    @Override
    public String getUsage() {
        return "Type this in to calm down :zzz:";
    }
}
