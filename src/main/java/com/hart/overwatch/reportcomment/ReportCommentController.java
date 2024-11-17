package com.hart.overwatch.reportcomment;

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
import com.hart.overwatch.reportcomment.request.CreateReportCommentRequest;
import com.hart.overwatch.reportcomment.response.CreateReportCommentResponse;
import com.hart.overwatch.reportcomment.response.DeleteReportCommentResponse;
import com.hart.overwatch.reportcomment.response.GetReportCommentsResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class ReportCommentController {

    private final ReportCommentService reportCommentService;

    @Autowired
    public ReportCommentController(ReportCommentService reportCommentService) {
        this.reportCommentService = reportCommentService;
    }

    @PostMapping("/report-comments")
    public ResponseEntity<CreateReportCommentResponse> createReportComment(
            @Valid @RequestBody CreateReportCommentRequest request) {
        reportCommentService.createReportComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateReportCommentResponse("success"));
    }

    @GetMapping("/admin/report-comments")
    public ResponseEntity<GetReportCommentsResponse> GetReportComments(
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetReportCommentsResponse("success",
                reportCommentService.getReportComments(page, pageSize, direction)));
    }

    @DeleteMapping("/admin/report-comments/{reportCommentId}")
    public ResponseEntity<DeleteReportCommentResponse> deleteReportComment(
            @PathVariable("reportCommentId") Long reportCommentId) {
        reportCommentService.deleteReportComment(reportCommentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new DeleteReportCommentResponse("success"));
    }
}
