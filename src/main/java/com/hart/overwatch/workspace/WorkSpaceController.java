package com.hart.overwatch.workspace;

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
import com.hart.overwatch.workspace.request.CreateWorkSpaceRequest;
import com.hart.overwatch.workspace.request.UpdateWorkSpaceRequest;
import com.hart.overwatch.workspace.response.CreateWorkSpaceResponse;
import com.hart.overwatch.workspace.response.DeleteWorkSpaceResponse;
import com.hart.overwatch.workspace.response.GetAllWorkSpaceResponse;
import com.hart.overwatch.workspace.response.UpdateWorkSpaceResponse;
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

    @GetMapping("")
    ResponseEntity<GetAllWorkSpaceResponse> getWorkSpaces(@RequestParam("userId") Long userId,
            @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetAllWorkSpaceResponse("success",
                this.workSpaceService.getWorkSpaces(userId, page, pageSize, direction)));
    }

    @PatchMapping("/{id}")
    ResponseEntity<UpdateWorkSpaceResponse> updateWorkSpace(
            @Valid @RequestBody UpdateWorkSpaceRequest request, @PathVariable("id") Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(new UpdateWorkSpaceResponse("success",
                this.workSpaceService.updateWorkSpace(request, id)));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<DeleteWorkSpaceResponse> deleteWorkSpace(@PathVariable("id") Long id) {

        this.workSpaceService.deleteWorkSpace(id);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteWorkSpaceResponse("success"));
    }
}
