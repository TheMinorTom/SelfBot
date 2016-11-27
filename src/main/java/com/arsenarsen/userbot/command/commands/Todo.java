package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.sql.SQL;
import com.arsenarsen.userbot.util.Messages;
import com.arsenarsen.userbot.util.VerticalAligner;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Todo implements Command {
    @Override
    public void dispatch(String[] args, MessageChannel channel, Message msg) {
        try {
            if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
                SQL.executeSQL(connection -> {
                    PreparedStatement st = connection.prepareStatement("INSERT INTO todo (task) VALUES (" +
                            "   ?" +
                            ")");
                    st.setString(1, Arrays.stream(Arrays.copyOfRange(args, 1, args.length)).collect(Collectors.joining(" ")));
                    st.executeUpdate();
                    msg.editMessage("Success!").queue();
                });
                return;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove") && args[1].matches("\\d*")) {
                    SQL.executeSQL(connection -> {
                        int id = Integer.parseInt(args[1]);
                        PreparedStatement st = connection.prepareStatement("DELETE FROM todo WHERE id = ?");
                        st.setInt(1, id);
                        st.execute();
                        msg.editMessage("Success!").queue();
                    });
                    return;
                }
            }
            SQL.executeSQL(connection -> {
                connection.createStatement().execute("CREATE TABLE IF NOT EXISTS todo(\n" +
                        "   id INTEGER PRIMARY KEY,\n" +
                        "   task VARCHAR(80) NOT NULL\n" +
                        ")");
                Statement s = connection.createStatement();
                s.execute("SELECT * FROM todo");
                ResultSet resultSet = s.getResultSet();
                StringBuilder todo = new StringBuilder().append("Your TODO: ```fix\n");
                Map<String, String> todos = new LinkedHashMap<>();
                while (resultSet.next()) {
                    todos.put(Integer.toString(resultSet.getInt("id")), resultSet.getString("task"));
                }
                todo.append(VerticalAligner.align(todos, "|"));
                todo.append("\n```");
                msg.editMessage(todo.toString()).queue();
            });
        } catch (SQLException e) {
            Messages.updateWithException("Could not get TODO", e, msg);
        }
    }

    @Override
    public String getName() {
        return "todo";
    }
}
