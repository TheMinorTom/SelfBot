package com.arsenarsen.userbot.sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents something for my db to do
 * <br>
 * Created by Arsen on 11.8.2016..
 */
@FunctionalInterface
public interface SQLTask {

    /**
     * SO a statement on the given connection
     *
     * @param toUse The connection
     * @throws SQLException If something goes wrong
     */
    void process(Connection toUse) throws SQLException;
}
