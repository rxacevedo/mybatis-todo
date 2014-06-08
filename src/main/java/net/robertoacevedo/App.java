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
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class App {

    public static void main(String[] args) {

        /** MyBatis - not handling some types well **/

        String resource = "mybatis-config.xml";

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

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
