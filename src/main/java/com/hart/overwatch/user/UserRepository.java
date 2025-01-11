package com.hart.overwatch.user;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.user.dto.MinUserDto;
import com.hart.overwatch.user.dto.ReviewerDto;
import com.hart.overwatch.user.dto.ViewUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query("SELECT u FROM User u LEFT JOIN FETCH u.blockerUsers WHERE u.id = :userId")
    User findUserWithBlockerUsers(@Param("userId") Long userId);


    @Query(value = """
                SELECT NEW com.hart.overwatch.user.dto.MinUserDto(
                 u.id AS id, u.fullName AS fullName, p.avatarUrl AS avatarUrl
                ) FROM User u
                INNER JOIN u.profile p
                WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                AND u.role <> 'ADMIN'
            """)
    Page<MinUserDto> getAllUsersBySearch(@Param("pageable") Pageable pageable,
            @Param("search") String search);

    @Query(value = """
                SELECT NEW com.hart.overwatch.user.dto.ReviewerDto(
                 u.id AS id, u.fullName AS fullName, p.avatarUrl AS avatarUrl
                ) FROM User u
                INNER JOIN u.profile p
                WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                AND u.role <> 'ADMIN'
                AND u.role = 'REVIEWER'
            """)
    Page<ReviewerDto> getReviewersBySearch(@Param("pageable") Pageable pageable,
            @Param("search") String search);

    @Query(value = """
                SELECT NEW com.hart.overwatch.user.dto.ViewUserDto(
                 u.id AS id, u.createdAt AS createdAt, u.firstName AS u.firstName,
                 u.lastName AS lastName, p.avatarUrl AS avatarUrl, u.role AS role,
                 u.email AS email
                ) FROM User u
                INNER JOIN u.profile p
            """)
    Page<ViewUserDto> getAllUsers(@Param("pageable") Pageable pageable);
}
