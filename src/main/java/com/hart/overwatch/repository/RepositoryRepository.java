package com.hart.overwatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RepositoryRepository extends JpaRepository<Repository, Long> {


    @Query(value = """
                     SELECT EXISTS(SELECT 1 FROM Repo
                       INNER JOIN r
                       INNER JOIN r.rev
                       WHERE o.id = 
                       AND  re.id = :re
                       AND  r.repoName = :
                 
                    """)
    boolean repositoryAlreadyInReview(@Param("ownerId") Long ownerId,
            
            @Param("reviewerId") Long reviewerId, @Param("repoName") String repoName);

}
