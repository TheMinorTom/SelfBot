package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.FileUtils;
import com.arsenarsen.userbot.util.Messages;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@SuppressWarnings("Duplicates")
public class Update implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            File tmp = new File(System.getProperty("java.io.tmpdir"));
            File repo = new File(tmp, "SelfBot" + System.currentTimeMillis());
            ProcessBuilder clone = new ProcessBuilder("git", "clone", "https://github.com/ArsenArsen/SelfBot.git", repo.getAbsolutePath())
                    .redirectErrorStream(true)
                    .directory(tmp);
            Process pClone = clone.start();
            String cloneLog = "";
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(pClone.getInputStream()))) {
                while (pClone.isAlive()) {
                    String line;
                    if ((line = reader.readLine()) != null) {
                        cloneLog += line + '\n';
                    }
                }
            }
            pClone.waitFor();
            if (pClone.exitValue() != 0) {
                Messages.edit(msg, "Failed to git clone!\n" + cloneLog);
                return;
            }
            ProcessBuilder build = new ProcessBuilder("mvn", "-U")
                    .redirectErrorStream(true)
                    .directory(repo);
            Process pBuild = build.start();
            String buildLog = "";
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(pBuild.getInputStream()))) {
                while (pBuild.isAlive()) {
                    String line;
                    if ((line = reader.readLine()) != null) {
                        buildLog += line + '\n';
                    }
                }
            }
            pBuild.waitFor();
            if (pBuild.exitValue() != 0) {
                Messages.edit(msg, "Failed to build!\n" + buildLog);
                return;
            }
            File current = new File(URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8")); // pfft this will go well..
            Files.copy(current.toPath(), Paths.get(current.getPath().replace(".jar", ".backup.jar")), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(repo, "target" + File.separator + "UserBot-jar-with-dependencies.jar").toPath(), current.toPath(), StandardCopyOption.REPLACE_EXISTING);
            FileUtils.delete(repo);
            System.exit(0);
        } catch (IOException | InterruptedException e) {
            Messages.updateWithException("Failed to update!", e, msg);
        }
    }

    @Override
    public String getName() {
        return "update";
    }
}
