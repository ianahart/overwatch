package com.hart.overwatch.testimonial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.testimonial.request.CreateTestimonialRequest;
import com.hart.overwatch.testimonial.response.CreateTestimonialResponse;
import com.hart.overwatch.testimonial.response.DeleteTestimonialResponse;
import com.hart.overwatch.testimonial.response.GetLatestTestimonialsResponse;
import com.hart.overwatch.testimonial.response.GetTestimonialsResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/testimonials")
public class TestimonialController {

    private TestimonialService testimonialService;

    @Autowired
    public TestimonialController(TestimonialService testimonialService) {
        this.testimonialService = testimonialService;
    }


    @DeleteMapping(path = "/{testimonialId}")
    public ResponseEntity<DeleteTestimonialResponse> deleteTestimonial(
            @PathVariable("testimonialId") Long testimonialId) {
        this.testimonialService.deleteTestimonial(testimonialId);

        return ResponseEntity.status(HttpStatus.OK).body(new DeleteTestimonialResponse("success"));
    }

    @PostMapping(path = "")
    public ResponseEntity<CreateTestimonialResponse> createTestimonial(
            @Valid @RequestBody CreateTestimonialRequest request) {

        this.testimonialService.createTestimonial(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateTestimonialResponse("success"));
    }

    @GetMapping(path = "")
    public ResponseEntity<GetTestimonialsResponse> getTestimonials(
            @RequestParam("userId") Long userId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetTestimonialsResponse("success",
                this.testimonialService.getTestimonials(userId, page, pageSize, direction)));
    }


    @GetMapping(path = "/latest")
    public ResponseEntity<GetLatestTestimonialsResponse> getLatestTestimonials(
            @RequestParam("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetLatestTestimonialsResponse(
                "success", this.testimonialService.getLatestTestimonials(userId)));
    }
}
