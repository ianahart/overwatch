package com.hart.overwatch.stripepaymentintent;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentDto;
import com.hart.overwatch.stripepaymentintent.projection.StripePaymentIntentApplicationFeeProjection;

@Repository
public interface StripePaymentIntentRepository extends JpaRepository<StripePaymentIntent, Long> {

    List<StripePaymentIntentApplicationFeeProjection> findAllBy();



    @Query(value = """
            SELECT new com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto(
             spi.id AS id, spi.amount AS amount, spi.applicationFee AS applicationFee,
             spi.createdAt AS createdAt, spi.currency AS currency, spi.description AS description,
             spi.status AS status, u.fullName AS userFullName, r.fullName AS reviewerFullName,
             u.email AS userEmail, r.email AS reviewerEmail, u.id AS userId, r.id AS reviewerId
            ) FROM StripePaymentIntent spi
            INNER JOIN spi.user u
            INNER JOIN spi.reviewer r
                WHERE (:search IS NULL OR
               LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(r.fullName) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<FullStripePaymentIntentDto> getStripePaymentIntentsBySearch(
            @Param("pageable") Pageable pageable, @Param("search") String search);

    @Query(value = """
                SELECT NEW com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentDto(
                spi.id AS id, spi.amount AS amount, spi.currency AS currency, r.fullName AS fullName,
                r.id AS reviewerId, p.avatarUrl AS avatarUrl, spi.createdAt AS createdAt, spi.status AS status
                ) FROM StripePaymentIntent spi
                INNER JOIN spi.user u
                INNER JOIN spi.reviewer r
                INNER JOIN spi.reviewer.profile p
                WHERE u.id = :userId
            """)
    Page<StripePaymentIntentDto> getStripePaymentIntentsByUserId(
            @Param("pageable") Pageable pageable, @Param("userId") Long userId);

}
