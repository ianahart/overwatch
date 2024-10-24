package com.hart.overwatch.topic;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByTitle(String title);


    @Query(value = "SELECT * FROM topic t WHERE t.search_vector @@ to_tsquery(:query)",
            nativeQuery = true)
    List<Topic> searchTopics(@Param("query") String query);
}
