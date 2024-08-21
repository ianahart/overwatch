package com.hart.overwatch.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSpaceRepository extends JpaRepository<WorkSpace, Long> {

    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM WorkSpace w
            INNER JOIN w.user u
            WHERE u.id = :userId
            AND w.title = :title
            )
            """)
    boolean alreadyExistsByTitleAndUserId(@Param("title") String title,
            @Param("userId") Long userId);



}
