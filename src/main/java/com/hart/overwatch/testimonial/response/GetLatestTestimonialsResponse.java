package com.hart.overwatch.testimonial.response;

import java.util.List;
import com.hart.overwatch.testimonial.dto.TestimonialDto;

public class GetLatestTestimonialsResponse {

    private String message;

    private List<TestimonialDto> data;


    public GetLatestTestimonialsResponse() {

    }

    public GetLatestTestimonialsResponse(String message, List<TestimonialDto> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public List<TestimonialDto> getData() {
        return data;
    }

    public void setData(List<TestimonialDto> data) {
        this.data = data;
    }


    public void setMessage(String message) {
        this.message = message;
    }

}
