package com.hart.overwatch.stripepaymentrefund;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StripePaymentRefundRepository extends JpaRepository<StripePaymentRefund, Long> {


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
