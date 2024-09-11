package com.hart.overwatch.checklist;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.checklist.dto.CheckListDto;

@Repository
public interface CheckListRepository extends JpaRepository<CheckList, Long> {

    List<CheckList> findByTodoCardId(Long todoCardId);

    @Query(value = """
            SELECT new com.hart.overwatch.checklist.dto.CheckListDto(
            cl.id AS id, u.id AS userId, tc.id AS todoCardId, cl.title AS title,
            cl.isCompleted AS isCompleted, cl.createdAt AS createdAt
            ) FROM CheckList cl
            INNER JOIN cl.todoCard tc
            INNER JOIN cl.user u
            WHERE tc.id = :todoCardId
            """)
    Page<CheckListDto> getCheckListsByTodoCardId(@Param("todoCardId") Long todoCardId,
            @Param("pageable") Pageable pageable);

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
