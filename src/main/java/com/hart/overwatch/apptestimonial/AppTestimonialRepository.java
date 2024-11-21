package com.hart.overwatch.apptestimonial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppTestimonialRepository extends JpaRepository<AppTestimonial, Long> {

    @Query(value = """
          SELECT at FROM AppTestimonial at
          INNER JOIN at.user u
          WHERE u.id = :userId
        """)
    AppTestimonial getAppTestimonialByUserId(@Param("userId") Long userId);

    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM AppTestimonial at
                INNER JOIN at.user u
                WHERE u.id = :userId
                )
            """)
    boolean existsByUserId(@Param("userId") Long userId);
}
