package com.hart.overwatch.todocard;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.todocard.dto.TodoCardDto;

@Repository
public interface TodoCardRepository extends JpaRepository<TodoCard, Long> {


    @Query(value = """
                SELECT new com.hart.overwatch.todocard.dto.TodoCardDto(
                tc.id AS id, tl.id AS todoListId, u.id AS userId,
                tc.createdAt AS createdAt, tc.label AS label,
                tc.title AS title, tc.color AS color, tc.index AS index,
                tc.details AS details, tc.startDate AS startDate,
                tc.endDate AS endDate
                ) FROM TodoCard tc
                INNER JOIN tc.user u
                INNER JOIN tc.todoList tl
                WHERE u.id = :userId
                AND tl.id = :todoListId
            """)
    TodoCardDto retrieveTodoCard(@Param("todoListId") Long todoListId,
            @Param("userId") Long userId);


    @Query(value = """
                SELECT new com.hart.overwatch.todocard.dto.TodoCardDto(
                tc.id AS id, tl.id AS todoListId, u.id AS userId,
                tc.createdAt AS createdAt, tc.label AS label,
                tc.title AS title, tc.color AS color, tc.index AS index,
                tc.details AS details, tc.startDate AS startDate,
                tc.endDate AS endDate
                ) FROM TodoCard tc
                INNER JOIN tc.user u
                INNER JOIN tc.todoList tl
                WHERE u.id = :userId
                AND tl.id = :todoListId
            """)
    List<TodoCardDto> retrieveTodoCards(@Param("todoListId") Long todoListId,
            @Param("userId") Long userId);

    @Query(value = """
               SELECT COUNT(tc.id) FROM TodoCard tc
                INNER JOIN tc.user u
                INNER JOIN tc.todoList tl
                WHERE u.id = :userId
                AND tl.id = :todoListId
            """)
    long countTodoCardsInTodoList(@Param("todoListId") Long todoListId,
            @Param("userId") Long userId);

}
