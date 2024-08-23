package com.hart.overwatch.todolist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {

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
