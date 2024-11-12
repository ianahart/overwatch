package com.hart.overwatch.stripepaymentintent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StripePaymentIntentRepository extends JpaRepository<StripePaymentIntent, Long> {
}
