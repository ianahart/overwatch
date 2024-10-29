package com.hart.overwatch.setting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hart.overwatch.setting.dto.SettingDto;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

    @Query(value = """
                SELECT new com.hart.overwatch.setting.dto.SettingDto(
                s.id AS id, u.id AS userId, s.mfaEnabled AS mfaEnabled,
                s.createdAt AS createdAt, s.reviewInProgressNotifOn AS reviewInProgressNotifOn,
                s.reviewInCompleteNotifOn AS reviewInCompleteNotifOn, s.reviewCompletedNotifOn AS reviewCompletedNotifOn,
                s.paymentAcknowledgementNotifOn AS paymentAcknowledgementNotifOn, s.requestPendingNotifOn AS requestPendingNotifOn,
               s.requestAcceptedNotifOn AS requestAcceptedNotifOn, s.commentReplyOn AS commentReplyOn
                ) FROM Setting s
                INNER JOIN s.user u
                WHERE s.id = :settingId
            """)
    SettingDto fetchSettingById(@Param("settingId") Long settingId);
}

