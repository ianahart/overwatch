package com.hart.overwatch.githubtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface GitHubTokenRepository extends JpaRepository<GitHubToken, Long> {

    @Modifying
    @Query(value = """
                DELETE FROM GitHubToken ght
                WHERE ght.user.id = :userId
            """)
    void deleteByUserId(@Param("userId") Long userId);

}
