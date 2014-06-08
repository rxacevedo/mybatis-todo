package net.robertoacevedo.domain;

import net.robertoacevedo.domain.Todo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

/**
 * Created by roberto on June 07, 2014.
 */
public interface TodoMapper {

    Todo selectTodo(@Param("id") UUID id);

    List<Todo> selectAllTodos();

}
