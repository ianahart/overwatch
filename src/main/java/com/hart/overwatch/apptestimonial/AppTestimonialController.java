package com.hart.overwatch.apptestimonial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.apptestimonial.request.CreateAppTestimonialRequest;
import com.hart.overwatch.apptestimonial.response.CreateAppTestimonialResponse;
import com.hart.overwatch.apptestimonial.response.GetSingleAppTestimonialResponse;
import com.hart.overwatch.apptestimonial.response.UpdateAppTestimonialResponse;
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

    @GetMapping(path = "/single")
    public ResponseEntity<GetSingleAppTestimonialResponse> getAppTestimonial() {
        return ResponseEntity.status(HttpStatus.OK).body(new GetSingleAppTestimonialResponse(
                "success", appTestimonialService.getAppTestimonial()));
    }

    @PatchMapping(path = "/{appTestimonialId}")
    public ResponseEntity<UpdateAppTestimonialResponse> updateAppTestimonial(
            @Valid @RequestBody CreateAppTestimonialRequest request,
            @PathVariable("appTestimonialId") Long appTestimonialId) {
        appTestimonialService.updateAppTestimonial(request, appTestimonialId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UpdateAppTestimonialResponse("success"));
    }
}
