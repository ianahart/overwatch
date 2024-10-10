package com.hart.overwatch.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.hart.overwatch.repository.dto.CompletedRepositoryReviewDto;
import com.hart.overwatch.repository.dto.RepositoryDto;
import com.hart.overwatch.repository.dto.RepositoryLanguageDto;
import com.hart.overwatch.repository.dto.RepositoryStatusDto;
import com.hart.overwatch.repository.dto.RepositoryTopRequesterDto;


public interface RepositoryRepository extends JpaRepository<Repository, Long> {


    @Query(value = """
             SELECT new com.hart.overwatch.repository.dto.RepositoryTopRequesterDto(
             o.id AS ownerId, o.fullName AS fullName, r.reviewStartTime AS reviewStartTime
             ) FROM Repository r
             INNER JOIN r.owner o
             INNER JOIN r.reviewer re
             WHERE re.id = :reviewerId
             AND r.reviewStartTime BETWEEN :startOfMonth AND :endOfMonth
            """)
    List<RepositoryTopRequesterDto> findOwnersByReviewerId(@Param("reviewerId") Long reviewerId,
            @Param("startOfMonth") LocalDateTime startOfMonth,
            @Param("endOfMonth") LocalDateTime endOfMonth);

    @Query(value = """
            SELECT new com.hart.overwatch.repository.dto.RepositoryStatusDto(
             r.status AS status
            ) FROM Repository r
            INNER JOIN r.reviewer re
            WHERE re.id = :reviewerId
            """)
    List<RepositoryStatusDto> findStatusesByReviewerId(@Param("reviewerId") Long reviewerId);

    @Query(value = """
            SELECT new com.hart.overwatch.repository.dto.RepositoryLanguageDto(
                 r.language AS language
              ) FROM Repository r
            INNER JOIN r.reviewer re
            WHERE re.id = :reviewerId
            """)
    List<RepositoryLanguageDto> getMainLanguages(@Param("reviewerId") Long reviewerId);

    @Query(value = """
            SELECT new com.hart.overwatch.repository.dto.CompletedRepositoryReviewDto(
              r.id AS id, r.reviewEndTime AS reviewEndTime, r.reviewType AS reviewType,
              reviewStartTime AS reviewStartTime
            ) FROM Repository r
            INNER JOIN r.reviewer re
            WHERE re.id = :reviewerId
            AND r.status = :status
            """)
    List<CompletedRepositoryReviewDto> findByReviewerIdAndCompleted(
            @Param("reviewerId") Long reviewerId, @Param("status") RepositoryStatus status);

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
