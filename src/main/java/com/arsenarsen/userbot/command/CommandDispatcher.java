package com.arsenarsen.userbot.command;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.commands.*;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * ayy commands
 * <br>
 * Created by Arsen on 21.9.16..
 */
public class CommandDispatcher extends ListenerAdapter {

    private Map<String, Command> commands = new HashMap<>();
    private ExecutorService threadPoolExecutor = Executors.newCachedThreadPool();

    public CommandDispatcher() {
        registerCommand(new JavaREPL());
        registerCommand(new Flippin());
        registerCommand(new Quote());
        registerCommand(new Update());
        registerCommand(new Todo());
        registerCommand(new AFK());
        registerCommand(new Execute());
        registerCommand(new HelpCommand());
        registerCommand(new Reboot());
        registerCommand(new Embed());
    }

    public boolean registerCommand(Command command) {
        if (command.getName().contains(" ")) {
            throw new IllegalArgumentException("Name must not have spaces!");
        }
        if (commands.containsKey(command.getName().toLowerCase())) {
            return false;
        }
        commands.put(command.getName().toLowerCase(), command);
        return true;
    }

    public List<String> getCommands() {
        return commands.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message msg = event.getMessage();
        String content = msg.getRawContent().trim();
        String prefix = UserBot.getInstance().getConfig().getProperty("prefix");
        if (author.getId().equals(event.getJDA().getSelfUser().getId())
                && content.toLowerCase().startsWith(prefix.toLowerCase())) {
            MessageChannel channel = event.getChannel();
            for (Command c : commands.values()) {
                if (content.toLowerCase().startsWith(prefix.toLowerCase() + c.getName() + ' ')
                        || content.equalsIgnoreCase(prefix + c.getName())) {
                    String[] split = split(content, c, prefix);
                    UserBot.LOGGER.info("Dispatching command '" + c.getName().toLowerCase() + "' with split: " + Arrays.toString(split));
                    threadPoolExecutor.submit(() -> c.dispatch(split, channel, msg));
                    break;
                }
            }
        }
    }

    private String[] split(String content, Command c, String prefix) {
        content = content.substring(c.getName().length() + prefix.length());
        if (content.startsWith(" ")) {
            content = content.substring(1);
        }
        if (content.length() == 0) {
            return new String[0];
        }
        return content.split("\\s");
    }

}
