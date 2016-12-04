package com.arsenarsen.userbot.command.commands;

import com.arsenarsen.userbot.command.Command;
import com.arsenarsen.userbot.sql.SQL;
import com.arsenarsen.userbot.util.DiscordUtils;
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
            SQL.executeSQL(connection -> connection.createStatement().execute("CREATE TABLE IF NOT EXISTS todo(\n" +
                    "   id INTEGER PRIMARY KEY,\n" +
                    "   task VARCHAR(80) NOT NULL\n" +
                    ")"));
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("clear")) {
                    SQL.executeSQL(connection -> {
                        connection.createStatement().execute("DELETE FROM todo");
                        msg.editMessage("Cleared!").queue();
                    });
                }
            } else if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
                SQL.executeSQL(connection -> {
                    PreparedStatement st = connection.prepareStatement("INSERT INTO todo (task) VALUES (" +
                            "   ?" +
                            ")");
                    final String[] task = {Arrays.stream(args).skip(1).collect(Collectors.joining(" "))};
                    msg.getMentionedUsers().forEach(user -> task[0] = task[0].replace(user.getAsMention(), user.getName() + '#' + user.getDiscriminator()));
                    msg.getMentionedUsers().forEach(user -> task[0] = task[0].replace("<@!" + user.getId() + ">", user.getName() + '#' + user.getDiscriminator()));
                    msg.getMentionedRoles().forEach(role -> task[0] = task[0].replace(role.getAsMention(), role.getName()));
                    msg.getMentionedChannels().forEach(textChannel -> task[0] = task[0].replace(textChannel.getAsMention(), textChannel.getName()));
                    st.setString(1, task[0]);
                    st.executeUpdate();
                    msg.editMessage("Success!").queue();
                });
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("remove") && args[1].matches("\\d*")) {
                    SQL.executeSQL(connection -> {
                        int id = Integer.parseInt(args[1]);
                        PreparedStatement st = connection.prepareStatement("DELETE FROM todo WHERE id = ?");
                        st.setInt(1, id);
                        st.execute();
                        msg.editMessage("Success!").queue();
                    });
                }
            } else {
                SQL.executeSQL(connection -> {
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
            }
        } catch (SQLException e) {
            DiscordUtils.updateWithException("Could not get TODO", e, msg);
        }
    }

    @Override
    public String getName() {
        return "todo";
    }

    @Override
    public String getUsage() {
        return "Saves a TODO list for ya :o Usage: todo add TODO; just todo to list them; todo remove ID to remove a TODO";
    }
}
