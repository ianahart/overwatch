package com.hart.overwatch.reportcomment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.reportcomment.response.CreateReportCommentResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/report-comments")
public class ReportCommentController {

    private final ReportCommentService reportCommentService;

    @Autowired
    public ReportCommentController(ReportCommentService reportCommentService) {
        this.reportCommentService = reportCommentService;
    }

    @PostMapping("")
    public ResponseEntity<CreateReportCommentResponse> createReportComment(
            @Valid @RequestBody CreateReportCommentRequest request) {
        reportCommentService.createReportComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateReportCommentResponse("success"));
    }
}
