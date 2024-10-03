package com.hart.overwatch.repository;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.repository.request.CreateRepositoryFileRequest;
import com.hart.overwatch.repository.request.CreateUserRepositoryRequest;
import com.hart.overwatch.repository.request.UpdateRepositoryCommentRequest;
import com.hart.overwatch.repository.request.UpdateRepositoryReviewRequest;
import com.hart.overwatch.repository.request.UpdateRepositoryReviewStartTimeRequest;
import com.hart.overwatch.repository.response.CreateRepositoryFileResponse;
import com.hart.overwatch.repository.response.CreateUserRepositoryResponse;
import com.hart.overwatch.repository.response.DeleteRepositoryResponse;
import com.hart.overwatch.repository.response.GetAllRepositoriesResponse;
import com.hart.overwatch.repository.response.GetDistinctRepositoryLanguagesResponse;
import com.hart.overwatch.repository.response.GetRepositoryCommentResponse;
import com.hart.overwatch.repository.response.GetRepositoryReviewResponse;
import com.hart.overwatch.repository.response.UpdateRepositoryCommentResponse;
import com.hart.overwatch.repository.response.UpdateRepositoryReviewResponse;
import com.hart.overwatch.repository.response.UpdateRepositoryReviewStartTimeResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/repositories")
public class RepositoryController {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }


    @PostMapping("/user")
    ResponseEntity<CreateUserRepositoryResponse> createUserRepository(
            @Valid @RequestBody CreateUserRepositoryRequest request) {
        this.repositoryService.handleCreateUserRepository(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateUserRepositoryResponse("success"));
    }

    @GetMapping("/languages")
    ResponseEntity<GetDistinctRepositoryLanguagesResponse> getDistinctRepositoryLanguages() {
        return ResponseEntity.status(HttpStatus.OK).body(new GetDistinctRepositoryLanguagesResponse(
                "success", this.repositoryService.getDistinctRepositoryLanguages()));
    }

    @GetMapping("")
    ResponseEntity<GetAllRepositoriesResponse> getAllRepositories(@RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction,
            @RequestParam("sort") String sort, @RequestParam("status") RepositoryStatus status,
            @RequestParam("language") String language) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetAllRepositoriesResponse("success", this.repositoryService
                        .getAllRepositories(page, pageSize, direction, sort, status, language)));
    }

    @DeleteMapping("/{repositoryId}")
    ResponseEntity<DeleteRepositoryResponse> deleteRepository(
            @PathVariable("repositoryId") Long repositoryId) {
        this.repositoryService.deleteRepository(repositoryId);

        return ResponseEntity.status(HttpStatus.OK).body(new DeleteRepositoryResponse("success"));
    }

    @GetMapping("/{repositoryId}/comment")
    ResponseEntity<GetRepositoryCommentResponse> getRepositoryComment(
            @PathVariable("repositoryId") Long repositoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(new GetRepositoryCommentResponse("success",
                this.repositoryService.getRepositoryComment(repositoryId)));
    }

    @PatchMapping("/{repositoryId}/comment")
    ResponseEntity<UpdateRepositoryCommentResponse> updateRepositoryComment(
            @PathVariable("repositoryId") Long repositoryId,
            @RequestBody UpdateRepositoryCommentRequest request) {
        this.repositoryService.updateRepositoryComment(repositoryId, request.getComment());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UpdateRepositoryCommentResponse("success"));
    }

    @GetMapping("/{repositoryId}")
    ResponseEntity<GetRepositoryReviewResponse> getRepositoryReview(
            @PathVariable("repositoryId") Long repositoryId,
            @RequestHeader("GitHub-Token") String gitHubAccessToken, @RequestParam("page") int page,
            @RequestParam("size") int size) throws IOException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetRepositoryReviewResponse("success", this.repositoryService
                        .getRepositoryReview(repositoryId, gitHubAccessToken, page, size)));
    }

    @PostMapping("/file")
    ResponseEntity<CreateRepositoryFileResponse> createRepositoryFile(
            @RequestBody CreateRepositoryFileRequest request) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(new CreateRepositoryFileResponse("success",
                this.repositoryService.getRepositoryFile(request)));
    }

    @PatchMapping("/{repositoryId}/starttime")
    ResponseEntity<UpdateRepositoryReviewStartTimeResponse> updateRepositoryReviewStartTime(
            @PathVariable("repositoryId") Long repositoryId,
            @RequestBody UpdateRepositoryReviewStartTimeRequest request) {
        this.repositoryService.updateRepositoryReviewStartTime(repositoryId, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new UpdateRepositoryReviewStartTimeResponse("success"));
    }

    @PatchMapping("/{repositoryId}")
    ResponseEntity<UpdateRepositoryReviewResponse> updateRepositoryReview(
            @PathVariable("repositoryId") Long repositoryId,
            @RequestBody UpdateRepositoryReviewRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateRepositoryReviewResponse(
                "success", this.repositoryService.updateRepositoryReview(repositoryId, request)));
    }

}
