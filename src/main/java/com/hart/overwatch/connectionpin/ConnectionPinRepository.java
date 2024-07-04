package com.hart.overwatch.connectionpin;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.connection.dto.ConnectionDto;
import com.hart.overwatch.connectionpin.dto.ConnectionPinDto;

@Repository
public interface ConnectionPinRepository extends JpaRepository<ConnectionPin, Long> {

    List<ConnectionPin> findConnectionPinByOwnerId(Long ownerId);


    @Query(value = """
            SELECT new com.hart.overwatch.connectionpin.dto.ConnectionPinDto(
             c.id AS id, cp.id AS connectionPinId, pi.id AS receiverId, o.id AS senderId, pi.firstName AS firstName,
             pi.lastName AS lastName, pr.avatarUrl AS avatarUrl, pr.email AS email,
            pl.city AS city, pl.country AS country, pr.contactNumber AS contactNumber, pr.bio AS bio
            ) FROM ConnectionPin cp
            INNER JOIN cp.owner o
            INNER JOIN cp.connection c
            INNER JOIN cp.pinned pi
            INNER JOIN cp.pinned.profile pr
            INNER JOIN cp.pinned.location pl
            WHERE o.id = :ownerId
            """)
    Page<ConnectionPinDto> getConnectionPinsByOwnerId(@Param("ownerId") Long ownerId,
            @Param("pageable") Pageable pageable);

    @Query(value = """
              SELECT COUNT(cp) FROM ConnectionPin cp
              INNER JOIN cp.owner o
              WHERE o.id = :ownerId
            """)
    Long countConnectionPinsByOwnerId(@Param("ownerId") Long ownerId);


    @Query(value = """
                SELECT EXISTS(SELECT 1 FROM ConnectionPin cp
                INNER JOIN cp.owner o
                INNER JOIN cp.pinned p
                INNER JOIN cp.connection c
                WHERE o.id = :ownerId
                AND p.id = :pinnedId
                AND c.id = :connectionId
                )
            """)
    boolean connectionPinAlreadyExists(@Param("ownerId") Long ownerId,
            @Param("pinnedId") Long pinnedId, @Param("connectionId") Long connectionId);
}
