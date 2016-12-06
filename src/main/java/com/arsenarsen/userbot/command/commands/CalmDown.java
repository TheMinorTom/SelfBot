package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class CalmDown implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            if(ThreadLocalRandom.current().nextBoolean()) {
                String calmnessSource =
                        Jsoup.connect("http://random.cat/").get().body().select("#cat").first().attr("abs:src");
                msg.editMessage("*Calm...*\n" + calmnessSource).queue();
            } else {
                String calmnessSource =
                        Jsoup.connect("http://random.dog/").get().body().select("body > p:nth-child(2) > img")
                                .first().attr("abs:src");
                msg.editMessage("*Calm...*\n" + calmnessSource).queue();
            }
        } catch (IOException e) {
            msg.editMessage("You cannot be calmed D:").queue();
            UserBot.LOGGER.error("I CANT CALM YOU D:", e);
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
