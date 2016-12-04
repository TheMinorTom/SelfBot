package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.DiscordUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Execute implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            String out = "";
            long a = System.currentTimeMillis();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                while (System.currentTimeMillis() - a < 15000 && p.isAlive()) {
                    if(Thread.currentThread().isInterrupted()){
                        break;
                    }
                    String line;
                    if ((line = reader.readLine()) != null)
                        out += line + '\n';
                    long modulus =  (System.currentTimeMillis() - a) % 2000;
                    if(modulus > 0 && modulus < 500){
                        DiscordUtils.edit(msg, "Evaluating...\n```\n" + out + "\n```");
                    }
                }
            }
            if (p.isAlive()){
                p.destroyForcibly();
                DiscordUtils.edit(msg, "Timed out!\n```\n"+out+"```");
                return;
            }
            DiscordUtils.edit(msg, "Evaluating...\n```\n" + out + "\n```Exit code: " + p.exitValue());
        } catch (IOException e) {
            UserBot.LOGGER.error("Could not exec!", e);
            DiscordUtils.updateWithException("Could not eval!", e, msg);
        }
    }

    @Override
    public String getName() {
        return "exec";
    }

    @Override
    public String getUsage() {
        return "Runs a command on your computer.";
    }
}
