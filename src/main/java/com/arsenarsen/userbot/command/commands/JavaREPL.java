package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.UserBot;
import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.util.FileUtils;
import com.arsenarsen.userbot.util.Messages;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.*;

/**
 * <br>
 * Created by Arsen on 21.9.16..
 */
public class JavaREPL implements Command {

    private String part1 = "", part2 = "";

    {
        InputStream p1 = JavaREPL.class.getClassLoader().getResourceAsStream("Pattern.java.part1");
        InputStream p2 = JavaREPL.class.getClassLoader().getResourceAsStream("Pattern.java.part2");
        BufferedReader p1reader = new BufferedReader(new InputStreamReader(p1));
        BufferedReader p2reader = new BufferedReader(new InputStreamReader(p2));
        String line;
        try {
            while ((line = p1reader.readLine()) != null) {
                part1 += line + System.getProperty("line.separator");
            }
        } catch (IOException e) {
            UserBot.LOGGER.error("Could not read Part1 of Pattern.java", e);
        }
        try {
            while ((line = p2reader.readLine()) != null) {
                part2 += line + System.getProperty("line.separator");
            }
        } catch (IOException e) {
            UserBot.LOGGER.error("Could not read Part2 of Pattern.java", e);
        }
    }

    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        String javaHome = System.getProperty("java.home");
        if (ToolProvider.getSystemJavaCompiler() == null) {
            System.setProperty("java.home", System.getenv("JDK_HOME"));
            if (ToolProvider.getSystemJavaCompiler() == null) {
                System.setProperty("java.home", System.getenv("JAVA_HOME"));
                if (ToolProvider.getSystemJavaCompiler() == null) {
                System.setProperty("java.home", javaHome);
                    msg.editMessage("You are missing JDK on your system! Halting..\n\n" +
                            "If you believe this is an error set JDK_HOME and JAVA_HOME enviromentals to point to it.").queue();
                    return;
                }
            }
        }
        if (args.length > 0) {
            String arg = msg.getRawContent().substring(UserBot.getInstance().getConfig().getProperty("prefix").length() + getName().length() + 1).trim();
            long time = System.currentTimeMillis();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            ExecutorService timer = Executors.newSingleThreadExecutor();
            try {
                msg.editMessage("Compiling!...").queue();
                File compileDir = new File(UserBot.WORKING_DIR, "compile");
                if (!compileDir.exists())
                    compileDir.mkdirs();
                File classStorage = new File(compileDir, "classes" + time);
                File classFile = new File(compileDir, "pkg" + time + File.separator + "Pattern.java");
                classStorage.mkdirs();
                classFile.getParentFile().mkdirs();
                classFile.createNewFile();
                Class<?> compiled = compile(arg, time, errorStream, classFile, classStorage);
                Runnable task = () -> {
                    try {
                        Method method = compiled.getDeclaredMethod("execute", MessageChannel.class);
                        msg.editMessage("Input: ```java\n" + arg + "\n```\n"
                                + "Output: " + method.invoke(null, channel)).queue();
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        Messages.updateWithException("Input: ```java\n" + arg + "\n```\n" + "Could not execute!\n", e, msg);
                    }
                };
                Future future = timer.submit(task);
                timer.shutdown();
                future.get(15, TimeUnit.SECONDS);
                if (!timer.isTerminated())
                    timer.shutdownNow();
                FileUtils.delete(classStorage);
                FileUtils.delete(classFile.getParentFile());
            } catch (IOException e) {
                Messages.updateWithException("Input: ```java\n" + arg + "\n```\n" + "Compilation failure!", e, msg);
            } catch (ClassNotFoundException ignored) {
                msg.editMessage("Input: ```java\n" + arg + "\n```\n" + "Could not compile!\n```\n" + errorStream + "\n```").queue();
            } catch (InterruptedException | ExecutionException | TimeoutException ignored) {
                msg.editMessage("Input: ```java\n" + arg + "\n```\n" + "Timeout!!").queue();
                if (!timer.isTerminated())
                    timer.shutdownNow();
            }
        } else msg.editMessage("Insert a short Java program to evaluate");

    }

    private Class<?> compile(String trim, long time, OutputStream compilerStream, File classFile, File classStorage) throws IOException, ClassNotFoundException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(classFile));
        writer.write("package pkg" + time + ";"
                + System.getProperty("line.separator") + part1 + trim + System.getProperty("line.separator") + part2);
        writer.flush();
        writer.close();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, compilerStream, compilerStream, classFile.getAbsolutePath(), "-d", classStorage.getAbsolutePath());
        URLClassLoader loader = new URLClassLoader(new URL[]{classStorage.toURI().toURL()}, getClass().getClassLoader());
        return loader.loadClass("pkg" + time + ".Pattern");
    }

    @Override
    public String getName() {
        return "java";
    }

    @Override
    public String getUsage() {
        return "Gigantic thing to compile and run Java.";
    }
}
