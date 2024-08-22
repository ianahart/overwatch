package com.hart.overwatch.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.workspace.dto.WorkSpaceDto;

@Repository
public interface WorkSpaceRepository extends JpaRepository<WorkSpace, Long> {


    @Query(value = """
            SELECT new com.hart.overwatch.workspace.dto.WorkSpaceDto(
            w.id AS id, u.id AS userId, w.createdAt AS createdAt, w.title AS title,
            w.backgroundColor AS backgroundColor
            ) FROM WorkSpace w
            INNER JOIN w.user u
            WHERE u.id = :userId
            """)
    Page<WorkSpaceDto> getWorkSpacesByUserId(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId);

    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM WorkSpace w
            INNER JOIN w.user u
            WHERE u.id = :userId
            AND w.title = :title
            )
            """)
    boolean alreadyExistsByTitleAndUserId(@Param("title") String title,
            @Param("userId") Long userId);

    @Query(value = """
               SELECT COUNT(w.id) FROM WorkSpace w
                INNER JOIN w.user u
                WHERE u.id = :userId
            """)
    long countWorkSpacesByUserId(@Param("userId") Long userId);


}
