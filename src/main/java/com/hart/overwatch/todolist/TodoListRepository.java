package com.hart.overwatch.todolist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.todolist.dto.TodoListDto;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {

    @Query(value = """
        SELECT new com.hart.overwatch.todolist.dto.TodoListDto(
        tl.id AS id, u.id AS userId, w.id AS workSpaceId,
        tl.title AS title, tl.index AS index, tl.createdAt AS createdAt
        ) FROM TodoList tl
        INNER JOIN tl.workSpace w
        INNER JOIN tl.user u
        WHERE w.id = :workSpaceId
        """)
    Page<TodoListDto> getTodoListsByWorkSpace(@Param("pageable") Pageable pageable, @Param("workSpaceId") Long workSpaceId);

    @Query(value = """
               SELECT COUNT(tl.id) FROM TodoList tl
                INNER JOIN tl.user u
                INNER JOIN tl.workSpace w
                WHERE u.id = :userId
                AND w.id = :workSpaceId
            """)
    long countTodoListsInWorkSpace(@Param("workSpaceId") Long workSpaceId,
            @Param("userId") Long userId);


    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM TodoList tl
            INNER JOIN tl.workSpace w
            INNER JOIN tl.user u
            WHERE u.id = :userId
            AND LOWER(tl.title) = :title
            AND w.id = :workSpaceId
            )
            """)
    boolean findTodoListByWorkSpaceAndUserAndTitle(@Param("workSpaceId") Long workSpaceId,
            @Param("userId") Long userId, @Param("title") String title);

}
