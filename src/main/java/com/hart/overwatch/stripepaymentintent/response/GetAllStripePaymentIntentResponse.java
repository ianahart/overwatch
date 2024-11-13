package com.hart.overwatch.stripepaymentintent.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentDto;

public class GetAllStripePaymentIntentResponse {

    private String message;

    private PaginationDto<StripePaymentIntentDto> data;

    public GetAllStripePaymentIntentResponse() {

    }

    public GetAllStripePaymentIntentResponse(String message,
            PaginationDto<StripePaymentIntentDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<StripePaymentIntentDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<StripePaymentIntentDto> data) {
        this.data = data;
    }
}
