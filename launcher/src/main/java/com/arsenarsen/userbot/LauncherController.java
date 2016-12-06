package com.arsenarsen.userbot;

import com.arsenarsen.userbot.websockets.WebsocketClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Future;

public class LauncherController {

    public static final File WORKING_DIR = new File(getAppData(), "Discord-UserBot");
    public static URL DOWNLOAD_URL = download();

    private static URL download() {
        URL download = null;
        try {
            download = new URL("https://ci.arsenarsen.com/job/SelfBot/lastSuccessfulBuild/artifact/target/UserBot-jar-with-dependencies.jar");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return download;
    }

    @FXML
    public Button stop;
    @FXML
    public Button start;
    @FXML
    public TextArea logbox;
    private Process selfbot;
    private WebsocketClient client;
    private boolean running = false;
    private static LauncherController instance;
    private volatile Future<?> task;

    public LauncherController() {
        super();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                getInstance().log("Cleaning up child processes....");
                getInstance().setRunning(false);
            } catch (InterruptedException ignored) {
            }
        }));
        Platform.runLater(() -> logbox.setEditable(false));
        instance = this;
    }

    public static LauncherController getInstance() {
        return instance;
    }

    @FXML
    public void onStart(ActionEvent actionEvent) {
        try {
            log("Starting!");
            File target = new File(WORKING_DIR, "UserBot.jar");
            if (!target.exists()) {
                target.getParentFile().mkdirs();
                target.createNewFile();
            }
            target.deleteOnExit();
            task = LauncherEntry.MAIN_POOL.submit(() -> {
                int status = 0;
                try {
                    setRunning(true);
                    while (isRunning() && status == 0 && !Thread.currentThread().isInterrupted()) {
                        URLConnection connection = DOWNLOAD_URL.openConnection();
                        connection.addRequestProperty("User-Agent", "Mozilla/5.0");
                        log("Downloading the latest jar....");
                        Files.copy(connection.getInputStream(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        log("Downloaded");
                        Platform.runLater(() -> stop.setDisable(false));
                        ProcessBuilder pb =
                                new ProcessBuilder("java", "-jar", target.getAbsolutePath());
                        pb.directory(WORKING_DIR);
                        pb.redirectErrorStream();
                        selfbot = pb.start();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(selfbot.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null && !Thread.currentThread().isInterrupted()) {
                                log("[UserBot] " + line);
                            }
                        }
                        selfbot.waitFor();
                        status = selfbot.exitValue();
                    }
                    setRunning(false);
                } catch (IOException e) {
                    LauncherEntry.LOGGER.error("Could not download/start the UserBot!", e);
                    Alert alert =
                            new Alert(Alert.AlertType.ERROR, "Could not download/start UserBot!\n" + e.getMessage());
                    alert.setResizable(true);
                    alert.show();
                    Platform.runLater(() -> stop.setDisable(true));
                    Platform.runLater(() -> start.setDisable(false));
                } catch (InterruptedException ignored) {
                    try {
                        setRunning(false);
                    } catch (InterruptedException ignored2) {
                    }
                }
            });
        } catch (IOException e) {
            LauncherEntry.LOGGER.error("Could not download the UserBot", e);
            Alert alert =
                    new Alert(Alert.AlertType.ERROR, "Could not download UserBot! " + e.getMessage());
            alert.setResizable(true);
            alert.show();
        }

    }

    public Process getSelfbot() {
        return selfbot;
    }

    public WebsocketClient getClient() {
        return client;
    }

    @FXML
    public void onStop(ActionEvent actionEvent) {
        try {
            setRunning(false);
        } catch (InterruptedException ignored) {
        }
    }

    public static File getAppData() {
        File wd;
        String OS = System.getProperty("os.name").toUpperCase();
        if (OS.contains("WIN")) {
            wd = new File(System.getenv("APPDATA"));
        } else if (OS.contains("MAC")) {
            wd = new File(System.getProperty("user.home") + "/Library/Application Support");
        } else if (OS.contains("NUX")) {
            wd = new File(System.getProperty("user.home"));
        } else {
            wd = new File(System.getProperty("user.home"));
        }
        return wd;
    }

    public void log(String log) {
        Platform.runLater(() -> logbox.appendText(log + '\n'));
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) throws InterruptedException {
        this.running = running;
        if (!running) {
            if (task != null)
                task.cancel(true);
            log("Stopping!");
            if (selfbot != null) {
                selfbot.destroyForcibly();
                selfbot.waitFor();
                log("Exited! Code: " + selfbot.exitValue());
                selfbot = null;
            }
        }
        Platform.runLater(() -> stop.setDisable(!running));
        Platform.runLater(() -> start.setDisable(running));
    }
}
