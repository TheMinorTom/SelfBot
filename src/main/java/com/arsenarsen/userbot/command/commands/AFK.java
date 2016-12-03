package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class AFK implements Command {
    public static AtomicBoolean afk = new AtomicBoolean(false);
    public static Set<String> mentioned = ConcurrentHashMap.newKeySet();
    public static volatile String afkReason = null;
    private OnlineStatus status = OnlineStatus.ONLINE;

    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        if (!afk.get()) {
            status = UserBot.getInstance().getJda().getPresence().getStatus();
            afk.set(true);
            if (args.length > 0)
                afkReason = msg.getRawContent().substring(1 + getName().length() + UserBot.getInstance().getConfig().getProperty("prefix").length());
            msg.editMessage("You're now AFK!" + (afkReason != null ? " (" + afkReason + ")" : "")).queue();
            UserBot.getInstance().getJda().getPresence().setStatus(OnlineStatus.IDLE);
        } else {
            UserBot.getInstance().getJda().getPresence().setStatus(status);
            afk.set(false);
            afkReason = null;
            mentioned.clear();
            msg.editMessage("You're no longer AFK!").queue();
        }
    }

    @Override
    public String getName() {
        return "afk";
    }

    @Override
    public String getUsage() {
        return "Lets you AFK :O Usage: afk REASON; Reason is optional";
    }
}
