package com.arsenarsen.userbot;

import com.arsenarsen.userbot.command.CommandDispatcher;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * ayy its mai bot
 * <br>
 * Created by Arsen on 21.9.16..
 */
public class UserBot {

    /* CONSTANTS */
    public static final Logger LOGGER = LoggerFactory.getLogger(UserBot.class);
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
            jda = new JDABuilder(AccountType.CLIENT).addListener((dispatcher = new CommandDispatcher())).setToken(token).buildAsync();
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

    private void saveDefaultConfig() throws IOException {
        // This is gonna hurt..
        Properties defaults = new Properties();
        defaults.setProperty("token", "INSERT YOUR TOKEN HERE");
        defaults.setProperty("id", "INSERT YOUR USER ID HERE");
        defaults.setProperty("prefix", "me.");

        defaults.store(new FileWriter(SETTINGS), "UserBot settings file");
    }

    public JDA getJda() {
        return jda;
    }

    public CommandDispatcher getDispatcher() {
        return dispatcher;
    }

    /* STATICS */
    public static void main(String... args) throws IOException {
        instance = new UserBot(args);
    }

    public static UserBot getInstance() {
        return instance;
    }
}
