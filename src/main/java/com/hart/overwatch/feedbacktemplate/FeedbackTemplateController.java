package com.hart.overwatch.feedbacktemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.feedbacktemplate.request.CreateFeedbackTemplateRequest;
import com.hart.overwatch.feedbacktemplate.response.CreateFeedbackTemplateResponse;
import com.hart.overwatch.feedbacktemplate.response.DeleteFeedbackTemplateResponse;
import com.hart.overwatch.feedbacktemplate.response.GetAllFeedbackTemplateResponse;
import com.hart.overwatch.feedbacktemplate.response.GetFeedbackTemplateResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/feedback-templates")
public class FeedbackTemplateController {

    private final FeedbackTemplateService feedbackTemplateService;

    @Autowired
    public FeedbackTemplateController(FeedbackTemplateService feedbackTemplateService) {
        this.feedbackTemplateService = feedbackTemplateService;
    }

    @PostMapping(path = "")
    public ResponseEntity<CreateFeedbackTemplateResponse> createFeedbackTemplate(
            @Valid @RequestBody CreateFeedbackTemplateRequest request) {
        feedbackTemplateService.createFeedbackTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateFeedbackTemplateResponse("success"));
    }

    @GetMapping(path = "/{feedbackTemplateId}")
    public ResponseEntity<GetFeedbackTemplateResponse> GetFeedbackTemplate(
            @PathVariable("feedbackTemplateId") Long feedbackTemplateId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetFeedbackTemplateResponse("success",
                feedbackTemplateService.getFeedbackTemplate(feedbackTemplateId)));
    }

    @GetMapping(path = "")
    public ResponseEntity<GetAllFeedbackTemplateResponse> getAllFeedbackTemplates() {
        return ResponseEntity.status(HttpStatus.OK).body(new GetAllFeedbackTemplateResponse(
                "success", feedbackTemplateService.getFeedbackTemplates()));
    }

    @DeleteMapping(path = "/{feedbackTemplateId}")
    public ResponseEntity<DeleteFeedbackTemplateResponse> deleteFeedbackTemplate(
            @PathVariable("feedbackTemplateId") Long feedbackTemplateId) {
        feedbackTemplateService.deleteFeedbackTemplate(feedbackTemplateId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DeleteFeedbackTemplateResponse("success"));
    }

}
