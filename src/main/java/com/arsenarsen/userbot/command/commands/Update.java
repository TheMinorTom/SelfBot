package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.Messages;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Update implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            File current = new File(URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8")); // pfft this will go well..
            Files.copy(current.toPath(), Paths.get(current.getPath().replace(".jar", ".backup.jar")), StandardCopyOption.REPLACE_EXISTING);
            URL url = new URL("https://ci.arsenarsen.com/job/SelfBot/lastSuccessfulBuild/artifact/target/UserBot-jar-with-dependencies.jar");
            URLConnection httpcon = url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
            try (FileOutputStream output = new FileOutputStream(current);
                 InputStream stream = httpcon.getInputStream()) {
                byte[] buff = new byte[2048];
                int len;
                int downloaded = 0;
                while ((len = stream.read(buff)) != -1) {
                    downloaded += len;
                    if (downloaded % 1024 * 1024 * 512 == 0)
                        UserBot.LOGGER.info("Updater: " + ((int) (downloaded / 102400f) / 100) + "K");
                    output.write(buff, 0, len);
                }
                output.flush();
            }
            Messages.edit(msg, "Aight! Exiting!");
            System.exit(0);
        } catch (Exception e) {
            Messages.updateWithException("Failed to update!", e, msg);
        }
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public String getUsage() {
        return "Updates the bot and exits. May not work on some platforms.";
    }
}
