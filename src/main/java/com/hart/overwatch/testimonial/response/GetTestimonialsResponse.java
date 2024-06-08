package com.hart.overwatch.testimonial.response;

import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.testimonial.dto.TestimonialDto;

public class GetTestimonialsResponse {

    private String message;
    private PaginationDto<TestimonialDto> data;

    public GetTestimonialsResponse() {

    }

    public GetTestimonialsResponse(String message, PaginationDto<TestimonialDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<TestimonialDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<TestimonialDto> data) {
        this.data = data;
    }
}
