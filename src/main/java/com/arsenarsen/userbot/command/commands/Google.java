package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.IOUtils;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Google implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if (args.length > 0) {
            String query = Arrays.stream(args).collect(Collectors.joining(" "));
            try {
                Document doc = Jsoup.
                        connect("https://www.google.com/search?q=" + URLEncoder.encode(query, "UTF-8"))
                        .userAgent("Mozilla/5.0 " + System.currentTimeMillis()).get();
                Elements ress = doc.getElementsByClass("g");
                String title = null;
                String url = null;
                String body = null;
                for (Element res : ress) {
                    try {
                        title = res.select(".r").first().text();
                        url = res.select(".r").first().children().first().attr("abs:href");
                        url = IOUtils.parse(url).get("q");
                        body = res.getElementsByClass("st").first().text();
                        break;
                    } catch (NullPointerException ignored) {
                    }
                }
                if (title == null || url == null || body == null) {
                    msg.editMessage("No results found!").queue();
                    return;
                }
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle(title);
                embedBuilder.setAuthor(doc.title().substring(0, doc.title().lastIndexOf("-") - 1), doc.location(), IOUtils.getIcon(doc.location()));
                embedBuilder.setDescription(body);
                embedBuilder.setUrl(url);
                embedBuilder.setColor(new Color((int) (0x1000000 * Math.random())));
                msg.editMessage(new MessageBuilder().setEmbed(embedBuilder.build()).build()).queue();
            } catch (URISyntaxException | IOException e) {
                DiscordUtils.updateWithException("Error occured!", e, msg);
            }
        }
    }


    @Override
    public String getName() {
        return "google";
    }

    @Override
    public String getUsage() {
        return "Does a google search of whatever you put in.";
    }
}
