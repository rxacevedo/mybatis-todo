package net.robertoacevedo;

import net.robertoacevedo.domain.Todo;
import net.robertoacevedo.domain.TodoEnumeration;
import net.robertoacevedo.domain.TodoMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class App {

    private static final String resource = "mybatis-config.xml";

    public static void main(String[] args) {

        try (InputStream configStream = Resources.getResourceAsStream(resource)) {

            /* Not AutoCloseable */
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configStream);
            SqlSession sqlSession = sqlSessionFactory.openSession();

            try (Connection connection = sqlSession.getConnection()) {

                List<Runnable> runnables = Arrays.asList(
                        () -> {
                            TodoMapper mapper = sqlSession.getMapper(TodoMapper.class);
                            List<Todo> mybatisTodoList = mapper.selectAllTodos();
                            mybatisTodoList.forEach(System.out::println);
                        },
                        (Runnable) System.out::println,
                        () -> {
                            Enumeration<Todo> enumeration = new TodoEnumeration(connection);
                            List<Todo> todoList = Collections.list(enumeration);
                            todoList.forEach(System.out::println);
                        }
                );

                runnables.forEach(Runnable::run);

                System.out.println();

                Callable<List<String>> getNames = () -> {
                    List<String> todoNames = Collections.list(new TodoEnumeration(connection))
                            .stream()
                            .map(Todo::getName)
                            .collect(Collectors.toCollection(ArrayList::new));
                    return todoNames;
                };

                try {
                    getNames.call().forEach(System.out::println);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
