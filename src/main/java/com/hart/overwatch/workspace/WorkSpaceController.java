package com.hart.overwatch.workspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.workspace.request.CreateWorkSpaceRequest;
import com.hart.overwatch.workspace.response.CreateWorkSpaceResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/workspaces")
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    @Autowired
    public WorkSpaceController(WorkSpaceService workSpaceService) {
        this.workSpaceService = workSpaceService;
    }

    @PostMapping("")
    ResponseEntity<CreateWorkSpaceResponse> createWorkSpace(
            @Valid @RequestBody CreateWorkSpaceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateWorkSpaceResponse("success",
                this.workSpaceService.createWorkSpace(request)));
    }
}
