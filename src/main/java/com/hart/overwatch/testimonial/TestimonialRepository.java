package com.hart.overwatch.testimonial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {


    @Query(value = """
              SELECT EXISTS(SELECT 1 FROM Testimonial t
               INNER JOIN t.user u
                WHERE LOWER(t.name) = :name
                AND u.id = :userId
                )
            """)

    boolean testimonialExists(@Param("name") String name, @Param("userId") Long userId);

}
