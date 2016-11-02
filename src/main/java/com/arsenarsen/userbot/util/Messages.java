package com.arsenarsen.userbot.util;

import com.arsenarsen.userbot.UserBot;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <br>
 * Created by Arsen on 22.9.16..
 */
public class Messages {

    public static void sendException(String msg, Throwable e, MessageChannel channel){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        UserBot.LOGGER.error(msg, e);
        channel.sendMessage(msg + ' ' + sw.toString()).queue();
        pw.close();
    }

    public static void updateWithException(String msg, Throwable e, Message mesg){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.close();
        UserBot.LOGGER.error(msg, e);
        String ctn = msg + "\n```" + sw.toString() + "\n```";
        if(ctn.length() > 2000){
            ctn = ctn.substring(0, 1995) + "\n```";
        }
        mesg.editMessage(ctn).queue();
        pw.close();
    }
}
