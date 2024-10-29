package com.hart.overwatch.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.connection.ConnectionService;
import com.hart.overwatch.connection.RequestStatus;
import com.hart.overwatch.connection.dto.MinConnectionDto;
import com.hart.overwatch.email.EmailQueueService;
import com.hart.overwatch.email.request.EmailRequest;
import com.hart.overwatch.notification.dto.MinNotificationDto;
import com.hart.overwatch.notification.dto.NotificationDto;
import com.hart.overwatch.notification.request.CreateNotificationRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.setting.Setting;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    private final ConnectionService connectionService;

    private final EmailQueueService emailQueueService;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
            UserService userService, PaginationService paginationService,
            ConnectionService connectionService, EmailQueueService emailQueueService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.paginationService = paginationService;
        this.connectionService = connectionService;
        this.emailQueueService = emailQueueService;
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
        Setting senderSetting = sender.getSetting();
        Setting receiverSetting = receiver.getSetting();

        switch (notificationType) {
            case REVIEW_INPROGRESS:
                if (receiverSetting.getReviewInProgressNotifOn()) {
                    notificationText.put("receiver",
                            String.format("You started to review one of %s's repositories.",
                                    sender.getFullName()));
                    queueEmail(receiver, "Review In Progress", notificationText.get("receiver"));
                }
                if (senderSetting.getReviewInProgressNotifOn()) {
                    notificationText.put("sender",
                            String.format("%s started to review one of your repositories.",
                                    receiver.getFullName()));
                    queueEmail(sender, "Review In Progress", notificationText.get("sender"));
                }
                break;
            case REVIEW_INCOMPLETE:
                if (receiverSetting.getReviewInCompleteNotifOn()) {
                    notificationText.put("receiver",
                            String.format("You accepted to review one of %s's repositories.",
                                    sender.getFullName()));
                    queueEmail(receiver, "Review Started", notificationText.get("receiver"));

                }
                if (senderSetting.getReviewInCompleteNotifOn()) {
                    notificationText.put("sender",
                            String.format("%s accepted to review one of your repositories.",
                                    receiver.getFullName()));
                    queueEmail(sender, "Review Started", notificationText.get("ssender"));

                }
                break;
            case REVIEW_COMPLETED:
                if (receiverSetting.getReviewCompletedNotifOn()) {
                    notificationText.put("receiver", String
                            .format("You completed %s's repository review.", sender.getFullName()));
                    queueEmail(receiver, "Review Completed", notificationText.get("receiver"));

                }
                if (senderSetting.getReviewCompletedNotifOn()) {
                    notificationText.put("sender",
                            String.format("%s completed a review on one of your repositories.",
                                    receiver.getFullName()));
                    queueEmail(sender, "Review Completed", notificationText.get("sender"));

                }
                break;
            case PAYMENT_ACKNOWLEDGEMENT:
                if (receiverSetting.getPaymentAcknowledgementNotifOn()) {
                    notificationText.put("receiver", "");
                }
                if (senderSetting.getPaymentAcknowledgementNotifOn()) {
                    notificationText.put("sender", "");
                }
                break;
            case CONNECTION_REQUEST_PENDING:
                if (receiverSetting.getRequestPendingNotifOn()) {
                    notificationText.put("receiver", String
                            .format("%s sent you a connection request.", sender.getFullName()));
                    queueEmail(receiver, "Connection Request", notificationText.get("receiver"));

                }
                if (senderSetting.getRequestPendingNotifOn()) {
                    notificationText.put("sender", String
                            .format("You sent %s a connection request.", receiver.getFullName()));
                    queueEmail(sender, "Connection Request", notificationText.get("sender"));

                }
                break;
            case CONNECTION_REQUEST_ACCEPTED:
                if (receiverSetting.getRequestAcceptedNotifOn()) {
                    notificationText.put("receiver",
                            String.format("You are now connected with %s.", sender.getFullName()));
                    queueEmail(receiver, "Connection Request Accepted",
                            notificationText.get("receiver"));

                }
                if (senderSetting.getRequestAcceptedNotifOn()) {
                    notificationText.put("sender", String.format(
                            "%s accepted your connection request.", receiver.getFullName()));
                    queueEmail(sender, "Connection Request Accepted",
                            notificationText.get("sender"));

                }
                this.connectionService.updateConnectionStatus(sender.getId(), receiver.getId(),
                        RequestStatus.ACCEPTED);
                deletePendingNotification(sender.getId(), receiver.getId(),
                        NotificationType.CONNECTION_REQUEST_PENDING);
                break;
            case COMMENT_REPLY:
                if (receiverSetting.getCommentReplyOn()) {
                    notificationText.put("receiver", String
                            .format("%s replied to one of your comments", sender.getFullName()));
                }
                notificationText.put("sender", "");
                break;
            default:
        }
        return notificationText;
    }


    private List<Notification> removeEmptyNotifications(List<Notification> notifications) {
        return notifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private NotificationDto convertToDto(Notification savedNotification, String avatarUrl) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(savedNotification.getId());
        notificationDto.setCreatedAt(savedNotification.getCreatedAt());
        notificationDto.setText(savedNotification.getText());
        notificationDto.setReceiverId(savedNotification.getReceiver().getId());
        notificationDto.setSenderId(savedNotification.getSender().getId());
        notificationDto.setAvatarUrl(avatarUrl);
        notificationDto.setNotificationType(savedNotification.getNotificationType());
        notificationDto.setNotificationRole(savedNotification.getNotificationRole());
        notificationDto.setLink(savedNotification.getLink());
        return notificationDto;
    }

    @Transactional
    private List<NotificationDto> saveAndRetrieveNotifications(List<Notification> notifications) {
        List<NotificationDto> retrievedNotifications = new ArrayList<>();
        List<Notification> filteredNotifications = removeEmptyNotifications(notifications);

        List<Notification> savedNotifications =
                this.notificationRepository.saveAll(filteredNotifications);

        for (Notification savedNotification : savedNotifications) {
            String avatarUrl = savedNotification.getNotificationRole() == NotificationRole.SENDER
                    ? savedNotification.getReceiver().getProfile().getAvatarUrl()
                    : savedNotification.getSender().getProfile().getAvatarUrl();
            NotificationDto retrievedNotification = convertToDto(savedNotification, avatarUrl);

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

            Notification senderNotification = null;
            Notification receiverNotification = null;
            String link = request.getLink().isPresent() ? request.getLink().get() : null;
            if (texts.get("sender") != null && texts.get("sender").length() > 0) {
                senderNotification =
                        new Notification(texts.get("sender"), request.getNotificationType(),
                                receiver, sender, NotificationRole.SENDER, link);
            }

            if (texts.get("receiver") != null && texts.get("receiver").length() > 0) {
                receiverNotification =
                        new Notification(texts.get("receiver"), request.getNotificationType(),
                                receiver, sender, NotificationRole.RECEIVER, link);
            }


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
                MinConnectionDto connectionDto =
                        this.connectionService.verifyConnection(senderId, receiverId);
                if (connectionDto.getStatus() == RequestStatus.PENDING) {
                    this.connectionService.deleteConnection(senderId, receiverId);
                }
            }
            deleteNotification(notificationId);

        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private void queueEmail(User recipient, String subject, String body) {
        if (body != null && !body.isEmpty()) {
            EmailRequest emailRequest = new EmailRequest(recipient.getEmail(), subject, body);
            emailQueueService.queueEmail(emailRequest);
        }
    }

}
