package com.hart.overwatch.stripepaymentintent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentDto;

@Repository
public interface StripePaymentIntentRepository extends JpaRepository<StripePaymentIntent, Long> {
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
