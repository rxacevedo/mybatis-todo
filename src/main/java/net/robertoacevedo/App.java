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
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;

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

                Enumeration<Todo> todoEnumeration = new TodoEnumeration(connection);
                Iterable<Todo> todoIterable = new TodoIterable(todoEnumeration);

                todoIterable.forEach(System.out::println);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
