package com.hart.overwatch.apptestimonial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.apptestimonial.dto.AppTestimonialDto;

@Repository
public interface AppTestimonialRepository extends JpaRepository<AppTestimonial, Long> {

    @Query(value = """
            SELECT new com.hart.overwatch.apptestimonial.dto.AppTestimonialDto(
            at.id AS id, u.firstName AS firstName, at.developerType AS developerType,
            at.content AS content, p.avatarUrl AS avatarUrl
            ) FROM AppTestimonial at
            INNER JOIN at.user u
            INNER JOIN at.user.profile p
            """)
    Page<AppTestimonialDto> getAppTestimonials(@Param("pageable") Pageable pageable);

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
