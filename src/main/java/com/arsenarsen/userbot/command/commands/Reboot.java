package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.Messages;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.IOException;
import java.net.URLDecoder;

public class Reboot implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar",
                    URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8"));
            pb.inheritIO();
            pb.start();
        } catch (IOException e) {
            Messages.updateWithException("Could not reboot!", e, msg);
            return;
        }
        System.exit(1);
    }

    @Override
    public String getName() {
        return "reboot";
    }
}
