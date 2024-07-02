package com.hart.overwatch.favorite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.favorite.dto.MinFavoriteDto;
import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


    @Query(value = """
            SELECT new com.hart.overwatch.favorite.dto.MinFavoriteDto(
            f.id AS id
            ) FROM Favorite f
            INNER JOIN f.user u
            INNER JOIN f.profile p
            WHERE u.id = :userId
            AND p.id = :profileId
            """)
    MinFavoriteDto getFavoriteByUserIdAndProfileId(@Param("userId") Long userId,
            @Param("profileId") Long profileId);

    @Query(value = """
              SELECT EXISTS(SELECT 1 FROM Favorite f
              INNER JOIN f.user u
              INNER JOIN f.profile p
              WHERE u.id = :userId
              AND p.id = :profileId
              )
            """)
    Boolean favoriteExists(@Param("userId") Long userId, @Param("profileId") Long profileId);


    @Query(value = """
            SELECT p.id FROM Favorite f
            INNER JOIN f.user u
            INNER JOIN f.profile p
            WHERE u.id = :userId
              """)
    Page<Long> getCurrentUserFavoriteIds(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId);
}
