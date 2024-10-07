package com.hart.overwatch.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hart.overwatch.repository.dto.RepositoryDto;


public interface RepositoryRepository extends JpaRepository<Repository, Long> {



    @Query(value = """
            SELECT NEW com.hart.overwatch.repository.dto.RepositoryDto(
             r.id AS id, o.id AS ownerId, re.id AS reviewerId, o.firstName AS firstName,
             o.lastName AS lastName, p.avatarUrl as profileUrl, r.repoName AS repoName,
             r.language AS language, r.repoUrl AS repoUrl, r.avatarUrl AS avatarUrl,
             r.createdAt AS createdAt, r.status AS status, r.reviewStartTime AS reviewStartTime,
            r.reviewEndTime AS reviewEndTime, r.feedback AS feedback
            ) FROM Repository r
            INNER JOIN r.reviewer re
            INNER JOIN r.owner o
            INNER JOIN r.owner.profile p
            WHERE re.id = :userId
            AND language = :language
            AND status = :status
            """)
    Page<RepositoryDto> getReviewerRepositoriesByLanguage(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("language") String language,
            @Param("status") RepositoryStatus status);


    @Query(value = """
             SELECT NEW com.hart.overwatch.repository.dto.RepositoryDto(
              r.id AS id, o.id AS ownerId, re.id AS reviewerId, o.firstName AS firstName,
              o.lastName AS lastName, p.avatarUrl as profileUrl, r.repoName AS repoName,
              r.language AS language, r.repoUrl AS repoUrl, r.avatarUrl AS avatarUrl,
              r.createdAt AS createdAt, r.status AS status, r.reviewStartTime AS reviewStartTime, r.reviewEndTime AS reviewEndTime,
               r.feedback AS feedback
             ) FROM Repository r
             INNER JOIN r.reviewer re
             INNER JOIN r.owner o
             INNER JOIN r.owner.profile p
             WHERE re.id = :userId
            AND r.status = :status
             """)
    Page<RepositoryDto> getAllReviewerRepositories(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("status") RepositoryStatus status);



    @Query(value = """
            SELECT NEW com.hart.overwatch.repository.dto.RepositoryDto(
             r.id AS id, o.id AS ownerId, re.id AS reviewerId, re.firstName AS firstName,
             re.lastName AS lastName, p.avatarUrl as profileUrl, r.repoName AS repoName,
             r.language AS language, r.repoUrl AS repoUrl, r.avatarUrl AS avatarUrl,
             r.createdAt AS createdAt, r.status AS status, r.reviewStartTime AS reviewStartTime, r.reviewEndTime AS reviewEndTime,
             r.feedback AS feedback
            ) FROM Repository r
            INNER JOIN r.reviewer re
            INNER JOIN r.owner o
            INNER JOIN r.reviewer.profile p
            WHERE o.id = :userId
            AND r.language = :language
            AND r.status = :status
            """)
    Page<RepositoryDto> getOwnerRepositoriesByLanguage(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("language") String language,
            @Param("status") RepositoryStatus status);


    @Query(value = """
            SELECT NEW com.hart.overwatch.repository.dto.RepositoryDto(
             r.id AS id, o.id AS ownerId, re.id AS reviewerId, re.firstName AS firstName,
             re.lastName AS lastName, p.avatarUrl as profileUrl, r.repoName AS repoName,
             r.language AS language, r.repoUrl AS repoUrl, r.avatarUrl AS avatarUrl,
             r.createdAt AS createdAt, r.status AS status, r.reviewStartTime AS reviewStartTime, r.reviewEndTime AS reviewEndTime, r.feedback AS feedback

            ) FROM Repository r
            INNER JOIN r.reviewer re
            INNER JOIN r.owner o
            INNER JOIN r.reviewer.profile p
            WHERE o.id = :userId
            AND r.status = :status
            """)
    Page<RepositoryDto> getAllOwnerRepositories(@Param("pageable") Pageable pageable,
            @Param("userId") Long userId, @Param("status") RepositoryStatus status);

    @Query(value = """
            SELECT DISTINCT r.language FROM Repository r
            INNER JOIN r.reviewer re
            WHERE re.id = :userId
            """)
    List<String> getReviewerDistinctRepositoryLanguages(@Param("userId") Long userId);


    @Query(value = """
            SELECT DISTINCT r.language FROM Repository r
            INNER JOIN r.owner o
            WHERE o.id = :userId
            """)
    List<String> getOwnerDistinctRepositoryLanguages(@Param("userId") Long userId);

    @Query(value = """
             SELECT EXISTS(SELECT 1 FROM Repository r
               INNER JOIN r.owner o
               INNER JOIN r.reviewer re
               WHERE o.id = :ownerId
               AND  re.id = :reviewerId
               AND  r.repoName = :repoName)
            """)
    boolean repositoryAlreadyInReview(@Param("ownerId") Long ownerId,
            @Param("reviewerId") Long reviewerId, @Param("repoName") String repoName);

}
