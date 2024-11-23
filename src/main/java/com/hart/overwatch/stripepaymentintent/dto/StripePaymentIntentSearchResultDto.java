package com.hart.overwatch.stripepaymentintent.dto;

import com.hart.overwatch.pagination.dto.PaginationDto;


public class StripePaymentIntentSearchResultDto {

    private PaginationDto<FullStripePaymentIntentDto> result;

    private Long revenue;

    public StripePaymentIntentSearchResultDto(PaginationDto<FullStripePaymentIntentDto> result,
            Long revenue) {

        this.result = result;
        this.revenue = revenue;
    }

    public Long getRevenue() {
        return revenue;
    }

    public PaginationDto<FullStripePaymentIntentDto> getResult() {
        return result;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public void setResult(PaginationDto<FullStripePaymentIntentDto> result) {
        this.result = result;
    }

}
