package com.arsenarsen.userbot.sql;

import com.arsenarsen.userbot.UserBot;
import org.sqlite.SQLiteConfig;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Handles the database connectivity
 */
public class SQL {

    private static File DATABASE = new File(UserBot.WORKING_DIR, "database.db");

    private static Connection getConnection() throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        SQLiteConnectionPoolDataSource dataSource = new SQLiteConnectionPoolDataSource();
        dataSource.setUrl("jdbc:sqlite:" + DATABASE.getAbsolutePath().replace("\\", "/"));
        dataSource.setConfig(config);
        return dataSource.getConnection();
    }

    /**
     * Safely does the SQL task
     *
     * @param toExec The tak to execute
     * @throws SQLException If something goes wrong+
     */
    public synchronized static void executeSQL(SQLTask toExec) throws SQLException {
        Connection toUse = getConnection();
        toExec.process(toUse);
        if (!toUse.isClosed()) {
            toUse.close();
        }
    }
}
