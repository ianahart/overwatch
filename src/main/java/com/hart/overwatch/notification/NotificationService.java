package com.hart.overwatch.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.notification.dto.NotificationDto;
import com.hart.overwatch.notification.request.CreateNotificationRequest;
import com.hart.overwatch.user.User;
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


    private Map<String, String> constructNotificationText(User sender, User receiver,
            NotificationType notificationType) {

        Map<String, String> notificationText = new HashMap<>();

        switch (notificationType) {
            case PAYMENT_ACKNOWLEDGEMENT:
                notificationText.put("receiver", "");
                notificationText.put("sender", "");
                break;
            case CONNECTION_REQUEST_PENDING:
                notificationText.put("receiver",
                        String.format("%s sent you a connection request.", sender.getFullName()));
                notificationText.put("sender",
                        String.format("You sent %s a connection request.", receiver.getFullName()));

                break;
            case CONNECTION_REQUEST_ACCEPTED:
                notificationText.put("receiver",
                        String.format("You are now connected with %s.", sender.getFullName()));
                notificationText.put("sender", String.format("%s accepted your connection request.",
                        receiver.getFullName()));
                break;
            default:

        }
        return notificationText;
    }



    @Transactional
    private List<NotificationDto> saveAndRetrieveNotifications(List<Notification> notifications) {
        List<NotificationDto> retrievedNotifications = new ArrayList<>();
        List<Notification> savedNotifications = this.notificationRepository.saveAll(notifications);

        for (Notification savedNotification : savedNotifications) {
            String avatarUrl = savedNotification.getNotificationRole() == NotificationRole.SENDER
                    ? savedNotification.getReceiver().getProfile().getAvatarUrl()
                    : savedNotification.getSender().getProfile().getAvatarUrl();
            NotificationDto retrievedNotification = new NotificationDto(savedNotification.getId(),
                    savedNotification.getCreatedAt(), savedNotification.getText(),
                    savedNotification.getReceiver().getId(), savedNotification.getSender().getId(),
                    avatarUrl, savedNotification.getNotificationType(),
                    savedNotification.getNotificationRole());

            retrievedNotifications.add(retrievedNotification);
        }

        return retrievedNotifications;
    }

    public List<NotificationDto> createNotification(CreateNotificationRequest request) {
        try {
            User sender = this.userService.getUserById(request.getSenderId());
            User receiver = this.userService.getUserById(request.getReceiverId());

            Map<String, String> texts =
                    constructNotificationText(sender, receiver, request.getNotificationType());

            Notification senderNotification = new Notification(texts.get("sender"),
                    request.getNotificationType(), receiver, sender, NotificationRole.SENDER);
            Notification receiverNotification = new Notification(texts.get("receiver"),
                    request.getNotificationType(), receiver, sender, NotificationRole.RECEIVER);


            List<Notification> notifications =
                    Arrays.asList(senderNotification, receiverNotification);

            List<NotificationDto> retrievedNotifications =
                    saveAndRetrieveNotifications(notifications);

            return retrievedNotifications;

        } catch (DataIntegrityViolationException ex) {

            throw new BadRequestException("Duplicate notifications emitted");
        }
    }
}
