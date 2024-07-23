package com.hart.overwatch.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.connection.ConnectionService;
import com.hart.overwatch.connection.RequestStatus;
import com.hart.overwatch.notification.dto.MinNotificationDto;
import com.hart.overwatch.notification.dto.NotificationDto;
import com.hart.overwatch.notification.request.CreateNotificationRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    private final ConnectionService connectionService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
            UserService userService, PaginationService paginationService,
            ConnectionService connectionService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.paginationService = paginationService;
        this.connectionService = connectionService;
    }


    private Notification getNotificationById(Long notificationId) {
        return this.notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(String
                        .format("A notification with the id %d was not found", notificationId)));
    }



    private void deletePendingNotification(Long senderId, long receiverId,
            NotificationType notificationType) {

        List<MinNotificationDto> notifications = this.notificationRepository
                .getNotificationBySenderIdAndReceiverId(senderId, receiverId, notificationType);

        for (MinNotificationDto notification : notifications) {
            this.notificationRepository.deleteById(notification.getId());
        }
    }

    private Map<String, String> constructNotificationText(User sender, User receiver,
            NotificationType notificationType) {

        Map<String, String> notificationText = new HashMap<>();

        switch (notificationType) {
            case REVIEW_INPROGRESS:
                notificationText.put("receiver", String.format(
                        "You started to review one of %s's repositories.", sender.getFullName()));
                notificationText.put("sender", String.format(
                        "%s started to review one of your repositories.", receiver.getFullName()));
                break;
            case REVIEW_INCOMPLETE:
                notificationText.put("receiver", String.format(
                        "You accepted to review one of %s's repositories.", sender.getFullName()));
                notificationText.put("sender", String.format(
                        "%s accepted to review one of your repositories.", receiver.getFullName()));
                break;
            case REVIEW_COMPLETED:
                notificationText.put("receiver", String
                        .format("You completed %s's repository review.", sender.getFullName()));
                notificationText.put("sender",
                        String.format("%s completed a review on one of your repositories.",
                                receiver.getFullName()));
                break;
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
                this.connectionService.updateConnectionStatus(sender.getId(), receiver.getId(),
                        RequestStatus.ACCEPTED);
                deletePendingNotification(sender.getId(), receiver.getId(),
                        NotificationType.CONNECTION_REQUEST_PENDING);
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


    public PaginationDto<NotificationDto> getAllNotifications(Long userId, int page, int pageSize,
            String direction) {

        Pageable pageable =

                this.paginationService.getSortedPageable(page, pageSize, direction, "desc");

        Page<NotificationDto> result =
                this.notificationRepository.getAllNotificationsByReceiverId(pageable, userId);


        return new PaginationDto<NotificationDto>(result.getContent(), result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());

    }


    private void deleteNotification(Long notificationId) {

        Optional<Notification> notification = this.notificationRepository.findById(notificationId);

        if (notification.isPresent()) {

            this.notificationRepository.delete(notification.get());
        }
    }

    public void handleDeleteNotification(NotificationRole notificationRole, Long senderId,
            Long receiverId, Long notificationId) {
        try {


            if (notificationRole == NotificationRole.RECEIVER) {

                this.connectionService.deleteConnection(senderId, receiverId);
            }

            deleteNotification(notificationId);

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}
