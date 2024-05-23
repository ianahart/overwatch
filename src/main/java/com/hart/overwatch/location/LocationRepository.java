package com.hart.overwatch.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.location.dto.LocationDto;


@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = """
            SELECT new com.hart.overwatch.location.dto.LocationDto(
             l.address AS address, l.addressTwo AS addressTwo, l.city AS city,
             l.country AS country, l.phoneNumber AS phoneNumber, l.state AS state,
             l.zipCode as zipCode
            ) FROM Location l
            INNER JOIN l.user u
            WHERE u.id = :userId
            """)
    LocationDto getFullLocationByUserId(@Param("userId") Long userId);


    @Query(value = """
              SELECT EXISTS(SELECT 1 FROM Location l
                INNER JOIN l.user u
                WHERE u.id = :userId
                )
            """)

    boolean userLocationExists(@Param("userId") Long userId);


    @Query(value = """
            SELECT l FROM Location l
            INNER JOIN l.user u
             WHERE u.id = :userId
            """)

    Location getLocationByUserId(@Param("userId") Long userId);

}
