package com.hart.overwatch.passwordreset;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {

    @Query(value = """
             SELECT pw FROM PasswordReset pw
             INNER JOIN pw.user u
            WHERE u.id = :userId
            """)
    PasswordReset getPasswordResetByUserId(@Param("userId") Long userId);

    Optional<PasswordReset> findByToken(String token);

    @Modifying
    @Query(value = """
                DELETE FROM password_reset pr
                WHERE pr.user_id = :id
            """, nativeQuery = true)
    @Transactional
    void deleteUserPasswordResetsById(@Param("id") Long id);
}

