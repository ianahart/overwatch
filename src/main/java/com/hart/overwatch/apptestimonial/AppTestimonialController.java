package com.hart.overwatch.apptestimonial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.apptestimonial.request.CreateAppTestimonialRequest;
import com.hart.overwatch.apptestimonial.response.CreateAppTestimonialResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/app-testimonials")
public class AppTestimonialController {

    private final AppTestimonialService appTestimonialService;

    @Autowired
    public AppTestimonialController(AppTestimonialService appTestimonialService) {
        this.appTestimonialService = appTestimonialService;
    }


    @PostMapping(path = "")
    public ResponseEntity<CreateAppTestimonialResponse> createAppTestimonial(
            @Valid @RequestBody CreateAppTestimonialRequest request) {
        appTestimonialService.createAppTestimonial(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAppTestimonialResponse("success"));
    }
}
