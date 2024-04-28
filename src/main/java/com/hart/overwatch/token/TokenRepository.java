package com.hart.overwatch.token;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
             SELECT expired, t.id, revoked, token, token_type, user_id
             FROM token t
             INNER JOIN _user u
             ON t.user_id = u.id
             WHERE T.user_id = :id
             AND t.expired = false
            OR t.revoked = false
                            """, nativeQuery = true)
    List<Token> findAllValidTokens(@Param("id") Long id);

    Optional<Token> findByToken(String token);

}

