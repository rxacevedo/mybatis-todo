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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

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

                Enumeration<Todo> todoEnumeration = new TodoEnumeration(connection);

                List<Todo> todoList = Collections.list(todoEnumeration);
                List<String> todoNames = todoList.stream().map(Todo::getName).collect(Collectors.toCollection(ArrayList::new));

                todoNames.forEach(System.out::println);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
