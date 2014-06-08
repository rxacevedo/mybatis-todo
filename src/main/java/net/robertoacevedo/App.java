package net.robertoacevedo;

import net.robertoacevedo.domain.Todo;
import net.robertoacevedo.domain.TodoEnumeration;
import net.robertoacevedo.domain.TodoIterable;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {

        /** MyBatis - not handling some types well **/

        String resource = "mybatis-config.xml";

        try (InputStream configStream = Resources.getResourceAsStream(resource)) {

            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configStream);
            SqlSession sqlSession = sqlSessionFactory.openSession();


            TodoMapper todoMapper = sqlSession.getMapper(TodoMapper.class);
            List<Todo> mybatisTodos = todoMapper.selectAllTodos();

            // System.out.println(mybatisTodo);
            mybatisTodos.forEach(System.out::println);

            System.out.println();

            /** JDBC **/

            try (Connection connection = sqlSession.getConnection()) {
                String sql = "SELECT * FROM items";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                Enumeration<Todo> todoEnumeration = new Enumeration<Todo>() {

                    private int columnIndex = 1;

                    @Override
                    public boolean hasMoreElements() {
                        boolean hasMoreElements = false;
                        try {
                            hasMoreElements = rs.next();
                        } catch (SQLException e) {
                            e.printStackTrace();
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
                };

                Iterable<Todo> todoIterable = new Iterable<Todo>() {
                    class TodoIterator implements Iterator<Todo> {

                        Enumeration<Todo> enumeration = todoEnumeration;

                        @Override
                        public boolean hasNext() {
                            return enumeration.hasMoreElements();
                        }

                        @Override
                        public Todo next() {
                            return enumeration.nextElement();
                        }

                        @Override
                        public void remove() {
                            // No
                        }

                        @Override
                        public void forEachRemaining(Consumer<? super Todo> action) {
                            // Need to read about this
                        }
                    }

                    @Override
                    public Iterator<Todo> iterator() {
                        return new TodoIterator();
                    }
                };

                todoIterable.forEach(System.out::println);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
