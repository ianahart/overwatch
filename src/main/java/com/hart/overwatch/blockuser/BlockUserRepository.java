package com.hart.overwatch.blockuser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.blockuser.dto.BlockUserDto;

@Repository
public interface BlockUserRepository extends JpaRepository<BlockUser, Long> {

    @Query(value = """
            SELECT new com.hart.overwatch.blockuser.dto.BlockUserDto(
             bu.id AS id, blkdu.id AS blockedUserId, bu.createdAt AS createdAt,
             blkdu.fullName AS fullName, p.avatarUrl AS avatarUrl
            ) FROM BlockUser bu
            INNER JOIN bu.blockerUser blkeru
            INNER JOIN bu.blockedUser blkdu
            INNER JOIN bu.blockedUser.profile p
            WHERE blkeru.id = :blockerUserId
                """)
    Page<BlockUserDto> getAllBlockedUsers(@Param("pageable") Pageable pageable,
            @Param("blockerUserId") Long blockerUserId);

    @Query(value = """
              SELECT EXISTS( SELECT 1 FROM BlockUser bu
                 INNER JOIN bu.blockerUser blkeru
                 INNER JOIN bu.blockedUser blkdu
                 WHERE blkeru.id = :blockerUserId
                 AND blkdu.id = :blockedUserId
              )
            """)
    boolean findByBlockedUserIdAndBlockerUserId(@Param("blockedUserId") Long blockedUserId,
            @Param("blockerUserId") Long blockerUserId);
}
