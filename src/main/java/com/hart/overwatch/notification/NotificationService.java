package com.hart.overwatch.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.user.UserService;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserService userService;


    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
            UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }


    private Notification getNotificationById(Long notificationId) {
        return this.notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("A notification with the id %d was not found", notificationId)));
    }


    public void createNotification() {
        System.out.println("Creating notification....");
    }
}
