package com.hart.overwatch.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT COUNT(u.id) FROM User u
            """)
    Long countUsers();

    Optional<User> findByEmail(String email);

    @Modifying
    @Query(value = """
            update User u set u.loggedIn = :loggedIn, u.lastActive = :lastActive WHERE u.id = :userId
            """)
    void updateLoggedIn(@Param("userId") Long userId, @Param("loggedIn") Boolean loggedIn,
            @Param("lastActive") LocalDateTime lastActive);

}
