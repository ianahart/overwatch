package com.hart.overwatch.label;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.label.dto.LabelDto;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    @Query(value = """
            SELECT new com.hart.overwatch.label.dto.LabelDto(
            l.id AS id, w.id AS workSpaceId, u.id AS userId, l.createdAt AS createdAt,
            l.isChecked AS isChecked, l.title AS title, l.color AS color
            ) FROM Label l
            INNER JOIN l.user u
            INNER JOIN l.workSpace w
            WHERE w.id = :workSpaceId
            """)
    Page<LabelDto> getLabelsByWorkSpaceId(@Param("workSpaceId") Long workSpaceId,
            @Param("pageable") Pageable pageable);

    @Query(value = """
               SELECT COUNT(l.id) FROM Label l
                INNER JOIN l.workSpace w
                WHERE w.id = :workSpaceId
            """)
    long countLabelsInWorkSpace(@Param("workSpaceId") Long workSpaceId);


    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM Label l
            INNER JOIN l.workSpace w
            WHERE w.id = :workSpaceId
            AND (LOWER(l.title) = :title OR l.color = :color)
             )
            """)
    boolean labelExistsInWorkSpace(@Param("color") String color, @Param("title") String title,
            @Param("workSpaceId") Long workSpaceId);


}
