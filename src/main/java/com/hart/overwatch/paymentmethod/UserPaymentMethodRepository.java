package com.hart.overwatch.paymentmethod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPaymentMethodRepository extends JpaRepository<UserPaymentMethod, Long> {



    @Query(value = """
            SELECT EXISTS(SELECT 1 FROM UserPaymentMethod upm
            INNER JOIN upm.user u
            WHERE u.id = :userId
            )
            """)
    boolean getUserPaymentMethodByUserId(@Param("userId") Long userId);

}
