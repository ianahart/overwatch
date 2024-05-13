package com.hart.overwatch.phone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.phone.dto.PhoneDto;


@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    @Modifying
    @Query("DELETE FROM Phone p WHERE p.id = :phoneId")
    @Transactional
    void deleteByPhoneId(@Param("phoneId") Long phoneId);


    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM Phone p
            INNER JOIN p.user u
            WHERE u.id = :userId
            )
            """)


    boolean existsByUserId(@Param("userId") Long userId);


    @Query(value = """
                SELECT new com.hart.overwatch.phone.dto.PhoneDto(
                 p.id AS id, p.createdAt as createdAt, p.isVerified as isVerified,
                p.phoneNumber AS phoneNumber
                ) FROM Phone p
                INNER JOIN p.user u
                WHERE u.id = :userId
                ORDER BY p.createdAt DESC
            """)
    PhoneDto getLatestPhoneByUserId(@Param("userId") Long userId);
}
