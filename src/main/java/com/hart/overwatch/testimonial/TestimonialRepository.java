package com.hart.overwatch.testimonial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.testimonial.dto.TestimonialDto;

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


    @Query(value = """
            SELECT new com.hart.overwatch.testimonial.dto.TestimonialDto(
            t.id AS id, u.id AS userId, t.name AS name, t.text AS text, t.createdAt AS createdAt
            ) FROM Testimonial t
            INNER JOIN t.user u
            WHERE u.id = :userId
            """)
    Page<TestimonialDto> getTestimonials(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId);
}
