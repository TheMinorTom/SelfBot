package com.arsenarsen.userbot.scheduler;

import java.util.*;

/**
 * Schedules tasks, wraps around Timer class<br>
 * Created by Arsen on 6.8.2016..
 */
public class Scheduler {
    private static final Timer timer = new Timer();
    private static final Map<String, TimerTask> tasks = new HashMap<>();

    /**
     * Schedules a repeating task
     *
     * @param task     The task to run
     * @param taskName Name to give to the task
     * @param delay    Time in milliseconds before the first run
     * @param interval Time between executions, in milliseconds
     * @return false if the task name already exists
     */
    public static boolean scheduleRepeating(Runnable task, String taskName, long delay, long interval) {
        if (tasks.containsKey(taskName)) {
            return false;
        }
        TimerTask toPut = new TimerTask() {

            @Override
            public void run() {
                task.run();
            }
        };
        timer.schedule(toPut, delay, interval);
        tasks.put(taskName, toPut);
        return true;
    }

    /**
     * Runs the task a bit later
     *
     * @param task  The task to run
     * @param delay Interval, in milliseconds
     */
    public static void delayTask(Runnable task, long delay) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, delay);
    }

    /**
     * Cancels a task
     *
     * @param taskName Tasks name
     * @return true, if the task was found and removed
     */
    public static boolean cancelTask(String taskName) {
        Iterator<Map.Entry<String, TimerTask>> i = tasks.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, TimerTask> next = i.next();
            if (next.getKey().equals(taskName)) {
                next.getValue().cancel();
                i.remove();
                return true;
            }
        }
        return false;
    }
}
