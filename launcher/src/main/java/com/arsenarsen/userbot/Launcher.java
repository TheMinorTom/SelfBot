package com.arsenarsen.userbot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.arsenarsen.userbot.LauncherEntry.MAIN_POOL;

public class Launcher extends Application {
    private static Launcher instance;
    private LauncherController controller;

    public static Launcher getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        //noinspection ConstantConditions
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("window.fxml"));
        Scene scene = new Scene(root, 611, 400);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> MAIN_POOL.shutdownNow());
        primaryStage.setTitle("UserBot Launcher");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public LauncherController getController() {
        return controller;
    }

    public static void entry(String[] args) {
        launch(args);
    }
}
