package com.arsenarsen.userbot;

import com.arsenarsen.userbot.command.CommandDispatcher;
import com.arsenarsen.userbot.command.commands.AFK;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Properties;

/**
 * ayy its mai bot
 * <br>
 * Created by Arsen on 21.9.16..
 */
public class UserBot extends ListenerAdapter {

    /* CONSTANTS */
    public static final Logger LOGGER = LoggerFactory.getLogger(UserBot.class);
    public static final String VERSION = getVersion(); // Inflated because java wont let me do otherwise /shrug

    private static String getVersion() {
        Properties p = new Properties();
        try {
            p.load(new InputStreamReader(UserBot.class.getClassLoader().getResourceAsStream("version.properties")));
        } catch (Exception e) {
            LOGGER.error("Could not load version!", e);
        }
        return p.getProperty("version", "RESOLUTION-FAILED");
    }

    public static final File WORKING_DIR = new File("UserBot" + File.separator);
    public static final File SETTINGS = new File(WORKING_DIR, "settings.properties");

    /* INSTANCE */
    private static UserBot instance;

    /* INSTANCE VARIABLES */
    private JDA jda;
    private Properties config = null;
    private CommandDispatcher dispatcher;

    /* LOCKS */
    private UserBot() {
    }

    /* METHODS */
    private UserBot(String... args) throws IOException {
        String token;
        if (!WORKING_DIR.exists()) {
            WORKING_DIR.mkdirs();
        }
        if (!SETTINGS.exists()) {
            SETTINGS.createNewFile();
            LOGGER.error("The config file has been created! Default values will be saved and the program will exit.");
            LOGGER.error("Please edit the config file to set your token.");
            saveDefaultConfig();
            System.exit(1);
            return;
        }
        token = getConfig().getProperty("token");
        try {
            jda = new JDABuilder(AccountType.CLIENT).addListener(this, (dispatcher = new CommandDispatcher())).setToken(token).buildAsync();
            jda.addEventListener(new ListenerAdapter() {
                @Override
                public void onMessageReceived(MessageReceivedEvent event) {
                    if (!event.getMessage().getMentionedUsers().contains(UserBot.getInstance().getJda().getSelfUser()))
                        return;
                    if (AFK.afk.get() && !AFK.mentioned.contains(event.getAuthor().getId())) {
                        AFK.mentioned.add(event.getAuthor().getId());
                        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " I am AFK!"
                                + (AFK.afkReason == null ? "" : "\nReason: " + AFK.afkReason)).queue();
                    }
                }
            });
        } catch (RateLimitedException | LoginException e) {
            LOGGER.error("Could not log in!", e);
        }
    }

    public Properties getConfig() {
        if (config == null) {
            config = new Properties();
            try {
                config.load(new FileReader(SETTINGS));
            } catch (IOException e) {
                LOGGER.error("Could not read settings!", e);
                config = null;
            }
        }
        return config;
    }

    public void saveConfig() {
        try {
            Writer writer = new FileWriter(SETTINGS);
            getConfig().store(writer, "UserBot settings file");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDefaultConfig() throws IOException {
        // This is gonna hurt..
        Properties defaults = new Properties();
        defaults.putAll(getConfig());
        defaults.setProperty("token", "INSERT YOUR TOKEN HERE");
        defaults.setProperty("prefix", "me.");
        Writer writer = new FileWriter(SETTINGS);
        defaults.store(writer, "UserBot settings file");
        writer.flush();
        writer.close();
    }

    public JDA getJda() {
        return jda;
    }

    public CommandDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public void onReady(ReadyEvent event) {
        LOGGER.info("Booted!");
    }

    /* STATICS */
    public static void main(String... args) throws IOException {
        instance = new UserBot(args);
    }

    public static UserBot getInstance() {
        return instance;
    }
}
