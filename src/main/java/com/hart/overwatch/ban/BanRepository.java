package com.hart.overwatch.ban;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.ban.dto.BanDto;

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
    @Query(value = """
            SELECT NEW com.hart.overwatch.ban.dto.BanDto(
             b.id AS id, u.fullName AS fullName, u.id AS userId,
             b.time AS time, b.adminNotes AS adminNotes, b.createdAt AS createdAt,
             b.banDate AS banDate, u.email AS email
            ) FROM Ban b
            INNER JOIN b.user u
            """)
    Page<BanDto> getBans(@Param("pageable") Pageable pageable);

    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM Ban b
            INNER JOIN b.user u
            WHERE u.id = :userId
            )
            """)
    boolean banExistsByUserId(@Param("userId") Long userId);
}
