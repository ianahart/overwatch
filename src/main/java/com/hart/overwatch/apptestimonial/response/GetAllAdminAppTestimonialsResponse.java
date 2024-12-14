package com.hart.overwatch.apptestimonial.response;

import com.hart.overwatch.apptestimonial.dto.AdminAppTestimonialDto;
import com.hart.overwatch.pagination.dto.PaginationDto;

public class GetAllAdminAppTestimonialsResponse {

    private String message;

    private PaginationDto<AdminAppTestimonialDto> data;

    public GetAllAdminAppTestimonialsResponse() {

    }

    public GetAllAdminAppTestimonialsResponse(String message,
            PaginationDto<AdminAppTestimonialDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public PaginationDto<AdminAppTestimonialDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(PaginationDto<AdminAppTestimonialDto> data) {
        this.data = data;
    }

}
