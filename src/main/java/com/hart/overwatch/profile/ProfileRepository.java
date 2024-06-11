package com.hart.overwatch.profile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.profile.dto.AllProfileDto;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {



    @Query(value = """
              SELECT new com.hart.overwatch.profile.dto.AllProfileDto(
               p.id AS id, u.id AS userId, p.fullName AS fullName, p.avatarUrl AS avatarUrl,
               l.country AS country, p.availability AS availability
              ) FROM Profile p
              INNER JOIN p.user u
              INNER JOIN p.user.location l
            """)
    Page<AllProfileDto> getMostRecent(@Param("pageable") Pageable pageable);
}
