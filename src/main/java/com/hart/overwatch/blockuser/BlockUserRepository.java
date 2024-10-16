package com.hart.overwatch.blockuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockUserRepository extends JpaRepository<BlockUser, Long> {

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
