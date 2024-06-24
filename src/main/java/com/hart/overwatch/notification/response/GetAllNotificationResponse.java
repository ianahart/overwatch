package com.hart.overwatch.notification.response;

import com.hart.overwatch.notification.dto.NotificationDto;
import com.hart.overwatch.pagination.dto.PaginationDto;

public class GetAllNotificationResponse {

    private String message;

    private PaginationDto<NotificationDto> data;

    public GetAllNotificationResponse() {

    }

    public GetAllNotificationResponse(String message, PaginationDto<NotificationDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<NotificationDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<NotificationDto> data) {
        this.data = data;
    }
}
