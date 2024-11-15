package com.hart.overwatch.stripepaymentrefund.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.stripepaymentrefund.dto.StripePaymentRefundDto;

public class GetAllStripePaymentRefundsResponse {

    private String message;

    private PaginationDto<StripePaymentRefundDto> data;

    public GetAllStripePaymentRefundsResponse() {

    }

    public GetAllStripePaymentRefundsResponse(String message,
            PaginationDto<StripePaymentRefundDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<StripePaymentRefundDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<StripePaymentRefundDto> data) {
        this.data = data;
    }


}
