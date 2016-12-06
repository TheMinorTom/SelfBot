package com.arsenarsen.userbot;

import com.arsenarsen.userbot.command.CommandDispatcher;
import com.arsenarsen.userbot.command.commands.AFK;
import com.arsenarsen.userbot.websocket.UserBotWebSocketServer;
import com.google.gson.Gson;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ayy its mai bot
 */
public class UserBot extends ListenerAdapter {

    /* CONSTANTS */
    private static final Map<String, Logger> LOGGERS = new ConcurrentHashMap<>();
    public static final Logger LOGGER = getLog(UserBot.class);
    public static final String VERSION = getVersion(); // Inflated because java wont let me do otherwise /shrug
    public static final Gson GSON = new Gson();

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
        SimpleLog.LEVEL = SimpleLog.Level.OFF;
        SimpleLog.addListener(new SimpleLog.LogListener() {
            @Override
            public void onLog(SimpleLog log, SimpleLog.Level logLevel, Object message) {
                switch (logLevel){
                    case ALL:
                    case INFO:
                        getLog(log.name).info(String.valueOf(message));
                        break;
                    case FATAL:
                        getLog(log.name).error(String.valueOf(message));
                        break;
                    case WARNING:
                        getLog(log.name).warn(String.valueOf(message));
                        break;
                    case DEBUG:
                        getLog(log.name).debug(String.valueOf(message));
                        break;
                    case TRACE:
                        getLog(log.name).trace(String.valueOf(message));
                        break;
                    case OFF:
                        break;
                }
            }

            @Override
            public void onError(SimpleLog log, Throwable err) {

            }
        });
        String token;
        if (!WORKING_DIR.exists()) {
            WORKING_DIR.mkdirs();
        }
        if (!SETTINGS.exists()) {
            SETTINGS.createNewFile();
            LOGGER.error("The config file has been created! Default values will be saved and the program will exit.");
            LOGGER.error("Please edit the config file to set your token");
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
            System.exit(1);
        }
        if(args.length == 1 && args[0].matches("[\\d]+"))
        try {
            new UserBotWebSocketServer(Integer.parseInt(args[0])).start();
        }catch (Exception t){
            LOGGER.error("Error starting websocket", t);
        }
    }

    private static Logger getLog(String name) {
        return LOGGERS.computeIfAbsent(name, LoggerFactory::getLogger);
    }

    public static Logger getLog(Class<?> clazz) {
        return getLog(clazz.getName());
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
        defaults.setProperty("downloadpath", "https://ci.arsenarsen.com/job/SelfBot/lastSuccessfulBuild/artifact/target/UserBot-jar-with-dependencies.jar");
        // Walsh its no longer hard coded :(
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
        LOGGER.info("Booted UserBot {}!", VERSION);
    }

    /* STATICS */
    public static void main(String... args) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> LOGGER.error("Uncaught error in " + t, e));
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> LOGGER.error("Uncaught error in " + t, e));
        instance = new UserBot(args);
    }

    public static UserBot getInstance() {
        return instance;
    }
}
