package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.Messages;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@SuppressWarnings("Duplicates")
public class Update implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            File current = new File(URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8")); // pfft this will go well..
            Files.copy(current.toPath(), Paths.get(current.getPath().replace(".jar", ".backup.jar")), StandardCopyOption.REPLACE_EXISTING);
            URL url = new URL("https://ci.arsenarsen.com/job/SelfBot/lastSuccessfulBuild/artifact/target/UserBot-jar-with-dependencies.jar");
            URLConnection httpcon = url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
            Files.copy(httpcon.getInputStream(), current.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Messages.edit(msg, "Aight! Rebooting!");
            System.exit(0);
        } catch (IOException e) {
            Messages.updateWithException("Failed to update!", e, msg);
        }
    }

    @Override
    public String getName() {
        return "update";
    }
}
