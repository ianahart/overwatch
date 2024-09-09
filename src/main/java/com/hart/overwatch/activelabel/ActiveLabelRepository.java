package com.hart.overwatch.activelabel;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.activelabel.dto.ActiveLabelDto;

@Repository
public interface ActiveLabelRepository extends JpaRepository<ActiveLabel, Long> {

    ActiveLabel findByTodoCardIdAndLabelId(Long todoCardId, Long labelId);

    @Query(value = """
                SELECT new com.hart.overwatch.activelabel.dto.ActiveLabelDto(
                 al.id AS id, tc.id AS todoCardId, l.id AS labelId,
                 l.color AS color, l.title AS title
                ) FROM ActiveLabel al
                INNER JOIN al.todoCard tc
                INNER JOIN al.label l
                WHERE tc.id = :todoCardId

            """)
    List<ActiveLabelDto> getActiveLabels(@Param("todoCardId") Long todoCardId);
}
