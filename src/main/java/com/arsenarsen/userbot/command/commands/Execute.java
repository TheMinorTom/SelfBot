package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.Messages;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Execute implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.redirectErrorStream(true);
            pb.directory(new File(System.getProperty("java.io.tmpdir")));
            Process p = pb.start();
            String out = "";
            long a = System.currentTimeMillis();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                while (System.currentTimeMillis() - a < 15000 && p.isAlive()) {
                    String line;
                    if ((line = reader.readLine()) != null)
                        out += line + '\n';
                }
            }
            if (p.isAlive()){
                p.destroyForcibly();
                Messages.edit(msg, "Timed out!");
                return;
            }
            Messages.edit(msg, "Evaluating...\n```\n" + out + "\n```Exit code: " + p.exitValue());
        } catch (IOException e) {
            UserBot.LOGGER.error("Could not exec!", e);
            Messages.updateWithException("Could not eval!", e, msg);
        }
    }

    @Override
    public String getName() {
        return "exec";
    }
}
