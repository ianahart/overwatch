package com.hart.overwatch.checklistitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckListItemRepository extends JpaRepository<CheckListItem, Long> {

    @Query(value = """
               SELECT COUNT(cli.id) FROM CheckListItem cli
                INNER JOIN cli.checkList cl
                WHERE cl.id = :checkListId
            """)
    long countCheckListItemsInCheckList(@Param("checkListId") Long checkListId);

    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM CheckListItem cli
                INNER JOIN cli.user u
                INNER JOIN cli.checkList cl
                WHERE u.id = :userId
                AND cl.id = :checkListId
                AND LOWER(cli.title) = LOWER(:title)
                )
            """)
    boolean checkListItemExistsByUserIdAndCheckListIdAndTitle(@Param("userId") Long userId,
            @Param("checkListId") Long checkListId, @Param("title") String title);

}
