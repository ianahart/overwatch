package com.hart.overwatch.activity;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.activity.dto.ActivityDto;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {


    @Query(value = """
            SELECT COUNT(a)
            FROM Activity a
            INNER JOIN a.user u
            WHERE u.id = :userId
            AND a.createdAt > :createdAt
            """)
    int countActivitiesByUserIdAndCreatedAtAfter(@Param("userId") Long userId,
            @Param("createdAt") LocalDateTime createdAt);


    @Query(value = """
            SELECT new com.hart.overwatch.activity.dto.ActivityDto(
             a.id AS id, u.id AS userId, tc.id AS todoCardId, a.createdAt AS createdAt,
             a.text AS text, p.avatarUrl AS avatarUrl
            ) FROM Activity a
            INNER JOIN a.user u
            INNER JOIN a.user.profile p
            INNER JOIN a.todoCard tc
            WHERE tc.id = :todoCardId

                """)
    Page<ActivityDto> getAllActivitiesByTodoCardId(@Param("todoCardId") Long todoCardId,
            @Param("pageable") Pageable pageable);
}
