package net.robertoacevedo.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by roberto on June 07, 2014.
 */
public class TodoEnumeration implements Enumeration<Todo> {

    private static final String SELECT_FROM_ITEMS = "SELECT * FROM items";

    private boolean isCalled = false;

    private int columnIndex = 1;
    private PreparedStatement ps;
    private ResultSet rs;

    public TodoEnumeration(final Connection connection) {
        try {
            ps = connection.prepareStatement(SELECT_FROM_ITEMS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasMoreElements() {

        if (!isCalled) {
            try {
                rs = ps.executeQuery();
                isCalled = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        boolean hasMoreElements = false;

        if (isCalled) {
            try {
                hasMoreElements = rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return hasMoreElements;
    }

    @Override
    public Todo nextElement() {
        Todo todo = new Todo();
        try {
            todo.setId((java.util.UUID) rs.getObject(columnIndex++));
            todo.setName(rs.getString(columnIndex++));
            todo.setDescription(rs.getString(columnIndex++));
            todo.setChecked(rs.getBoolean(columnIndex++));
            todo.setDateCreated(rs.getTimestamp(columnIndex++));
            columnIndex = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todo;
    }
}
