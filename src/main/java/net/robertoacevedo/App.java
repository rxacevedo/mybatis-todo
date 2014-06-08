package net.robertoacevedo;

import net.robertoacevedo.domain.Todo;
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
import java.util.List;
import java.util.UUID;

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
                while (rs.next()) {
                    Todo jdbcTodo = new Todo();
                    jdbcTodo.setId((UUID) rs.getObject("id"));
                    jdbcTodo.setName(rs.getString("name"));
                    jdbcTodo.setDescription(rs.getString("description"));
                    jdbcTodo.setChecked(rs.getBoolean("checked"));
                    jdbcTodo.setDateCreated(rs.getTimestamp("date_created"));
                    System.out.println(jdbcTodo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
