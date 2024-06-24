package com.hart.overwatch.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.notification.response.DeleteNotificationResponse;
import com.hart.overwatch.notification.response.GetAllNotificationResponse;

@RestController
@RequestMapping(path = "/api/v1/notifications")
public class NotificationController {


    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(path = "")
    public ResponseEntity<GetAllNotificationResponse> getAllNotifications(
            @RequestParam("userId") Long userId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllNotificationResponse("success",
                this.notificationService.getAllNotifications(userId, page, pageSize, direction)));
    }

    @DeleteMapping(path = "/{notificationId}")
    public ResponseEntity<DeleteNotificationResponse> deleteNotification(
            @RequestParam("receiverId") Long receiverId, @RequestParam("senderId") Long senderId,
            @RequestParam("notificationRole") NotificationRole notificationRole,
            @PathVariable("notificationId") Long notificationId) {
        this.notificationService.handleDeleteNotification(notificationRole, senderId, receiverId,
                notificationId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteNotificationResponse("success"));
    }
}
