package com.hart.overwatch.customfield;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFieldRepository extends JpaRepository<CustomField, Long> {

    @Query(value = """
             SELECT EXISTS(SELECT 1 FROM CustomField cf
             INNER JOIN cf.todoCard tc
             WHERE tc.id = :todoCardId
             AND (cf.fieldName = :fieldName AND cf.fieldType = :fieldType)
            )
            """)
    boolean alreadyExistsByFieldNameNotType(@Param("todoCardId") Long todoCardId,
            @Param("fieldName") String fieldName, @Param("fieldType") String fieldType);


    @Query(value = """
               SELECT COUNT(cf.id) FROM CustomField cf
                INNER JOIN cf.user u
                INNER JOIN cf.todoCard tc
                WHERE u.id = :userId
                AND tc.id = :todoCardId
            """)
    long countCustomFieldsPerTodoCard(@Param("userId") Long userId,
            @Param("todoCardId") Long todoCardId);

}
