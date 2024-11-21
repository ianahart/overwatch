package com.hart.overwatch.apptestimonial.response;

import com.hart.overwatch.apptestimonial.dto.MinAppTestimonialDto;

public class GetSingleAppTestimonialResponse {

    private String message;

    private MinAppTestimonialDto data;

    public GetSingleAppTestimonialResponse() {

    }

    public GetSingleAppTestimonialResponse(String message, MinAppTestimonialDto data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public MinAppTestimonialDto getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(MinAppTestimonialDto data) {
        this.data = data;
    }


}
