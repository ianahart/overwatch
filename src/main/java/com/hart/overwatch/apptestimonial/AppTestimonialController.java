package com.hart.overwatch.apptestimonial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.apptestimonial.request.CreateAppTestimonialRequest;
import com.hart.overwatch.apptestimonial.request.UpdateAdminAppTestimonialRequest;
import com.hart.overwatch.apptestimonial.response.CreateAppTestimonialResponse;
import com.hart.overwatch.apptestimonial.response.DeleteAppTestimonialResponse;
import com.hart.overwatch.apptestimonial.response.GetAllAdminAppTestimonialsResponse;
import com.hart.overwatch.apptestimonial.response.GetAllAppTestimonialsResponse;
import com.hart.overwatch.apptestimonial.response.GetSingleAppTestimonialResponse;
import com.hart.overwatch.apptestimonial.response.UpdateAdminAppTestimonialResponse;
import com.hart.overwatch.apptestimonial.response.UpdateAppTestimonialResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class AppTestimonialController {

    private final AppTestimonialService appTestimonialService;

    @Autowired
    public AppTestimonialController(AppTestimonialService appTestimonialService) {
        this.appTestimonialService = appTestimonialService;
    }


    @PostMapping(path = "/app-testimonials")
    public ResponseEntity<CreateAppTestimonialResponse> createAppTestimonial(
            @Valid @RequestBody CreateAppTestimonialRequest request) {
        appTestimonialService.createAppTestimonial(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateAppTestimonialResponse("success"));
    }

    @GetMapping(path = "/app-testimonials/single")
    public ResponseEntity<GetSingleAppTestimonialResponse> getAppTestimonial() {
        return ResponseEntity.status(HttpStatus.OK).body(new GetSingleAppTestimonialResponse(
                "success", appTestimonialService.getAppTestimonial()));
    }

    @PatchMapping(path = "/app-testimonials/{appTestimonialId}")
    public ResponseEntity<UpdateAppTestimonialResponse> updateAppTestimonial(
            @Valid @RequestBody CreateAppTestimonialRequest request,
            @PathVariable("appTestimonialId") Long appTestimonialId) {
        appTestimonialService.updateAppTestimonial(request, appTestimonialId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UpdateAppTestimonialResponse("success"));
    }

    @DeleteMapping(path = "/app-testimonials/{appTestimonialId}")
    public ResponseEntity<DeleteAppTestimonialResponse> deleteAppTestimonial(
            @PathVariable("appTestimonialId") Long appTestimonialId) {
        appTestimonialService.deleteAppTestimonial(appTestimonialId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DeleteAppTestimonialResponse("success"));
    }

    @GetMapping(path = "/app-testimonials")
    public ResponseEntity<GetAllAppTestimonialsResponse> getAppTestimonials(
            @RequestParam("pageSize") Integer pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllAppTestimonialsResponse(
                "success", appTestimonialService.getAppTestimonials(pageSize)));
    }

    @GetMapping(path = "/admin/app-testimonials")
    public ResponseEntity<GetAllAdminAppTestimonialsResponse> getAdminAppTestimonials(
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetAllAdminAppTestimonialsResponse("success",
                        appTestimonialService.getAdminAppTestimonials(page, pageSize, direction)));
    }

    @PatchMapping(path = "/admin/app-testimonials/{appTestimonialId}")
    public ResponseEntity<UpdateAdminAppTestimonialResponse> updateAdminAppTestimonial(
            @RequestBody UpdateAdminAppTestimonialRequest request,
            @PathVariable("appTestimonialId") Long appTestimonialId) {
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateAdminAppTestimonialResponse(
                "success",
                appTestimonialService.updateAdminAppTestimonial(request, appTestimonialId)));
    }
}
