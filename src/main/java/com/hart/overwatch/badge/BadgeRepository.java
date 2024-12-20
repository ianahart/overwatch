package com.hart.overwatch.badge;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.badge.dto.BadgeDto;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    @Query(value = """
              SELECT b FROM Badge b
             WHERE LOWER(b.title) = :title
            """)
    Optional<Badge> findBadgeByTitle(@Param("title") String title);


    @Query(value = """
            SELECT EXISTS( SELECT 1 FROM Badge b
             WHERE LOWER(b.title) = :title
            )
            """)
    boolean existsByTitle(@Param("title") String title);


    @Query(value = """
                SELECT NEW com.hart.overwatch.badge.dto.BadgeDto(
                b.id AS id, b.createdAt AS createdAt, b.title AS title,
                b.description AS description, b.imageUrl AS imageUrl
                ) FROM Badge b
            """)
    Page<BadgeDto> getBadges(@Param("pageable") Pageable pageable);

}
