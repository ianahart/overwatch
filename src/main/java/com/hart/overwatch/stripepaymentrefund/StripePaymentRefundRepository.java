package com.hart.overwatch.stripepaymentrefund;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.stripepaymentrefund.dto.StripePaymentRefundDto;

@Repository
public interface StripePaymentRefundRepository extends JpaRepository<StripePaymentRefund, Long> {
    @Query(value = """
            SELECT NEW com.hart.overwatch.stripepaymentrefund.dto.StripePaymentRefundDto(
             spr.id AS id, spr.adminNotes AS adminNotes, spr.amount AS amount, spr.currency AS currency,
             spr.reason AS reason, spr.status AS status, spi.id AS stripePaymentIntentId,
             spr.createdAt AS createdAt, u.id AS userId, u.fullName AS fullName
            ) FROM StripePaymentRefund spr
            INNER JOIN spr.user u
            INNER JOIN spr.stripePaymentIntent spi
            """)
    Page<StripePaymentRefundDto> findPaymentRefunds(@Param("pageable") Pageable pageable);


    @Query(value = """
                SELECT EXISTS(SELECT 1 FROM StripePaymentRefund spr
                INNER JOIN spr.user u
                INNER JOIN spr.stripePaymentIntent spi
                WHERE u.id = :userId
                AND spi.id = :stripePaymentIntentId
                )
            """)
    boolean findPaymentRefundByUserIdAndStripePaymentIntentId(@Param("userId") Long userId,
            @Param("stripePaymentIntentId") Long stripePaymentIntentId);
}
