package com.arsenarsen.userbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.arsenarsen.userbot.LauncherController.DOWNLOAD_URL;
import static com.arsenarsen.userbot.LauncherController.WORKING_DIR;

/**
 * I dont care I curse here. FUCKING JAVAFX
 */
public class LauncherEntry {
    public static final Gson GSON = new GsonBuilder().create();
    public static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);
    public static final ExecutorService MAIN_POOL = Executors.newCachedThreadPool();
    public static boolean nogui = false;
    private static Process selfbot = null;

    public static void main(String... args) throws IOException, InterruptedException {
        if (args.length > 0 && args[0].equalsIgnoreCase("nogui")) {
            noGui();
        } else Launcher.entry(args);
    }

    private static void noGui() throws InterruptedException, IOException {
        nogui = true;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (selfbot != null) selfbot.destroyForcibly();
        }));
        File target = new File(WORKING_DIR, "UserBot.jar");
        int status = 0;
        while (status == 0 && !Thread.currentThread().isInterrupted()) {
            URLConnection connection = DOWNLOAD_URL.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0");
            LOGGER.info("Downloading the latest jar....");
            Files.copy(connection.getInputStream(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Downloaded");
            ProcessBuilder pb =
                    new ProcessBuilder("java", "-jar", target.getAbsolutePath());
            pb.directory(WORKING_DIR);
            pb.redirectErrorStream();
            selfbot = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(selfbot.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                    LOGGER.info("[UserBot] " + line);
                }
            }
            selfbot.waitFor();
            status = selfbot.exitValue();
            LOGGER.info("UserBot exited with code {}!", status);
        }
    }
}
