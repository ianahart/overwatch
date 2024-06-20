package com.hart.overwatch.connection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {


    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM Connection c
             INNER JOIN c.sender s
             INNER JOIN c.receiver r
             WHERE r.id = :receiverId
             AND  s.id = :senderId
            )
            """)
    boolean findExistingConnection(@Param("receiverId") Long receiverId,
            @Param("senderId") Long senderId);
}
