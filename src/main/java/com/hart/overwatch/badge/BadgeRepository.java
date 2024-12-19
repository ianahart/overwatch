package com.hart.overwatch.badge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    @Query(value = """
            SELECT EXISTS( SELECT 1 FROM Badge b
             WHERE LOWER(b.title) = :title
            )
            """)
    boolean existsByTitle(@Param("title") String title);
}
