package com.hart.overwatch.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.repository.request.CreateUserRepositoryRequest;
import com.hart.overwatch.repository.response.CreateUserRepositoryResponse;
import com.hart.overwatch.repository.response.GetAllRepositoriesResponse;
import com.hart.overwatch.repository.response.GetDistinctRepositoryLanguagesResponse;
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


}
