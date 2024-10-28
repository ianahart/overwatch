package com.hart.overwatch.topic;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.topic.dto.TopicDto;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByTitle(String title);


    @Query(value = "SELECT * FROM topic t WHERE t.search_vector @@ to_tsquery(:query)",
            nativeQuery = true)
    List<Topic> searchTopics(@Param("query") String query);

    Page<Topic> findAll(@Param("pageable") Pageable pageable);

    @Query(value = """
            SELECT t FROM Topic t JOIN t.tags tag WHERE tag.name = :query
            """)
    Page<Topic> findTopicWithTags(@Param("pageable") Pageable pageable,
            @Param("query") String query);
}
