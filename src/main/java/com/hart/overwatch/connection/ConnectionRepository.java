package com.hart.overwatch.connection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.connection.dto.ConnectionDto;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    Connection findBySenderIdAndReceiverId(Long senderId, Long receiverId);


    @Query(value = """
            SELECT new com.hart.overwatch.connection.dto.ConnectionDto(
             c.id AS id, r.id AS receiverId, s.id AS senderId, r.firstName AS firstName,
             r.lastName AS lastName, rp.avatarUrl AS avatarUrl, rp.email AS email,
             rl.city AS city, rl.country AS country, rp.contactNumber as phoneNumber,
             rp.bio AS bio
            ) FROM Connection c
            INNER JOIN c.receiver r
            INNER JOIN c.sender s
            INNER JOIN r.profile rp
            INNER JOIN r.location rl
            WHERE s.id = :userId
            AND c.status = 'ACCEPTED'
                """)
    Page<ConnectionDto> getSenderConnections(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId);


    @Query(value = """
            SELECT new com.hart.overwatch.connection.dto.ConnectionDto(
             c.id AS id, r.id AS receiverId, s.id AS senderId, s.firstName AS firstName,
             s.lastName AS lastName, sp.avatarUrl AS avatarUrl, sp.email AS email,
             sl.city AS city, sl.country AS country, sp.contactNumber as phoneNumber,
             sp.bio AS bio
            ) FROM Connection c
            INNER JOIN c.receiver r
            INNER JOIN c.sender s
            INNER JOIN s.profile sp
            INNER JOIN s.location sl
            WHERE r.id = :userId
            AND c.status = 'ACCEPTED'
                """)
    Page<ConnectionDto> getReceiverConnections(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId);


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
