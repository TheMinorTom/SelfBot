package com.arsenarsen.userbot.guiutils;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

// hey you, feel free to copy paste this class where ever you need it. Link back my github pls :^)
// https://github.com/ArsenArsen
public class ExceptionBox extends Alert {
    public ExceptionBox(Throwable t, String header){
        super(AlertType.ERROR);
        setTitle("Exception Occured!");
        setHeaderText(header);
        setContentText(t.getLocalizedMessage());
        setResizable(true);
        TextArea exception = new TextArea();
        exception.setEditable(false);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.flush();
        exception.appendText(sw.toString());
        pw.close();
        exception.setEditable(false);
        exception.setMaxWidth(Double.MAX_VALUE);
        exception.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(exception, Priority.ALWAYS);
        GridPane.setHgrow(exception, Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(new Label("Stack Trace:"), 0, 0);
        expContent.add(exception, 0, 1);
        getDialogPane().setExpandableContent(expContent);
    }
}
