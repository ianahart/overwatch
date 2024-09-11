package com.hart.overwatch.checklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckListRepository extends JpaRepository<CheckList, Long> {
    @Query(value = """
               SELECT COUNT(cl.id) FROM CheckList cl
                INNER JOIN cl.todoCard tc
                WHERE tc.id = :todoCardId
            """)
    long countCheckListsInTodoCard(@Param("todoCardId") Long todoCardId);


    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM CheckList cl
                INNER JOIN cl.user u
                INNER JOIN cl.todoCard tc
                WHERE u.id = :userId
                AND tc.id = :todoCardId
                AND LOWER(cl.title) = LOWER(:title)
                )
            """)
    boolean checkListExistsByTitleAndUserIdAndTodoCardId(@Param("userId") Long userId,
            @Param("todoCardId") Long todoCardId, @Param("title") String title);
}
