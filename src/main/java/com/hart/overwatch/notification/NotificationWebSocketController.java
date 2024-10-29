package com.hart.overwatch.notification;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.hart.overwatch.notification.dto.NotificationDto;
import com.hart.overwatch.notification.request.CreateNotificationRequest;


@Controller
public class NotificationWebSocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final NotificationService notificationService;

    @Autowired
    public NotificationWebSocketController(SimpMessagingTemplate simpMessagingTemplate,
            NotificationService notificationService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificationService = notificationService;
    }

    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public void receiveNotification(@Payload CreateNotificationRequest request) {
        List<NotificationDto> notifications = this.notificationService.createNotification(request);

        for (NotificationDto notificationDto : notifications) {
            System.out.println(notificationDto.getText());
        }

        NotificationDto senderNotification = notifications.stream()
                .filter(v -> v.getNotificationRole() == NotificationRole.SENDER).findFirst()
                .orElse(null);

        NotificationDto receiverNotification = notifications.stream()
                .filter(v -> v.getNotificationRole() == NotificationRole.RECEIVER).findFirst()
                .orElse(null);

        if (senderNotification != null) {
            this.simpMessagingTemplate.convertAndSendToUser(String.valueOf(request.getSenderId()),
                    "/topic/notifications", senderNotification);
        }

        if (receiverNotification != null) {
            this.simpMessagingTemplate.convertAndSendToUser(String.valueOf(request.getReceiverId()),
                    "/topic/notifications", receiverNotification);
        }
    }
}
