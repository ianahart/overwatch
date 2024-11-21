package com.hart.overwatch.apptestimonial.response;

import java.util.List;
import com.hart.overwatch.apptestimonial.dto.AppTestimonialDto;

public class GetAllAppTestimonialsResponse {

    private String message;

    private List<AppTestimonialDto> data;

    public GetAllAppTestimonialsResponse() {

    }

    public GetAllAppTestimonialsResponse(String message, List<AppTestimonialDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<AppTestimonialDto> getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<AppTestimonialDto> data) {
        this.data = data;
    }
}
