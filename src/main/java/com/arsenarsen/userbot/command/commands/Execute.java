package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.Messages;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.*;

public class Execute implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            ProcessBuilder pb = new ProcessBuilder(args);
            pb.redirectErrorStream(true);
            pb.directory(new File(System.getProperty("java.io.tmpdir")));
            Process p = pb.start();
            String out = "";
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))){
                while(p.isAlive()){
                    String line;
                    if((line = reader.readLine()) != null)
                        out += line + '\n';
                }
            }
            p.waitFor();
            Messages.edit(msg, "Evaluating...\n```\n"+out+"\n```Exit code: " + p.exitValue());
        } catch (InterruptedException | IOException e){
            UserBot.LOGGER.error("Could not exec!", e);
            Messages.updateWithException("Could not eval!", e, msg);
        }
    }

    @Override
    public String getName() {
        return "exec";
    }
}
