package com.hart.overwatch.stripepaymentintent.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.stripepaymentintent.dto.FullStripePaymentIntentDto;
import com.hart.overwatch.stripepaymentintent.dto.StripePaymentIntentSearchResultDto;

public class GetAllSearchStripePaymentIntentResponse {

    private String message;

    private StripePaymentIntentSearchResultDto data;

    public GetAllSearchStripePaymentIntentResponse() {

    }

    public GetAllSearchStripePaymentIntentResponse(String message,
            StripePaymentIntentSearchResultDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public StripePaymentIntentSearchResultDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(StripePaymentIntentSearchResultDto data) {
        this.data = data;
    }
}
