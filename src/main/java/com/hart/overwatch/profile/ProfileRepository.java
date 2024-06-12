package com.hart.overwatch.profile;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {


    @Query(value = """
                SELECT p.id AS id, u.id AS userId, p.full_name AS fullName, p.avatar_url AS avatarUrl,
                       l.country AS country, p.availability AS availability, p.programming_languages AS programmingLanguages
                FROM _user u
                JOIN profile p ON u.profile_id = p.id
                JOIN location l ON u.id = l.user_id
                ORDER BY p.id DESC
            """,
            nativeQuery = true)
    Page<Map<String, Object>> getMostRecent(@Param("pageable") Pageable pageable);


    @Query(value = """
                SELECT p.id AS id, u.id AS userId, p.full_name AS fullName, p.avatar_url AS avatarUrl,
                       l.country AS country, p.availability AS availability, p.programming_languages AS programmingLanguages
                FROM _user u
                JOIN profile p ON u.profile_id = p.id
                JOIN location l ON u.id = l.user_id
                WHERE LOWER(l.country) = :full OR LOWER(l.country) = :abbrev
            """,
            nativeQuery = true)
    Page<Map<String, Object>> getDomestic(@Param("pageable") Pageable pageable,
            @Param("full") String full, @Param("abbrev") String abbrev);

}


