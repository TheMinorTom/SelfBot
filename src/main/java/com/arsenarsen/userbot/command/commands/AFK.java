package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.concurrent.atomic.AtomicBoolean;

public class AFK implements Command {
    public static AtomicBoolean afk = new AtomicBoolean(false);
    private OnlineStatus status = OnlineStatus.ONLINE;

    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if(!afk.get()){
            status = UserBot.getInstance().getJda().getPresence().getStatus();
            afk.set(true);
            msg.editMessage("You're now AFK!").queue();
            UserBot.getInstance().getJda().getPresence().setStatus(OnlineStatus.IDLE);
        } else {
            UserBot.getInstance().getJda().getPresence().setStatus(status);
            afk.set(false);
            msg.editMessage("You're no longer AFK!").queue();
        }
    }

    @Override
    public String getName() {
        return "afk";
    }
}
