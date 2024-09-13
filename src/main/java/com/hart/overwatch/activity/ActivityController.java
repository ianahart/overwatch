package com.hart.overwatch.activity;

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
import com.hart.overwatch.activity.request.CreateActivityRequest;
import com.hart.overwatch.activity.response.CreateActivityResponse;
import com.hart.overwatch.activity.response.DeleteActivityResponse;
import com.hart.overwatch.activity.response.FetchActivityResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1")
public class ActivityController {

    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/activities")
    public ResponseEntity<CreateActivityResponse> createActivity(
            @Valid @RequestBody CreateActivityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateActivityResponse("success",
                activityService.handleCreateActivity(request)));
    }

    @GetMapping("/todo-cards/{todoCardId}/activities")
    public ResponseEntity<FetchActivityResponse> getActivities(
            @PathVariable("todoCardId") Long todoCardId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {
        return ResponseEntity.status(HttpStatus.OK).body(new FetchActivityResponse("success",
                activityService.getActivities(todoCardId, page, pageSize, direction)));
    }

    @DeleteMapping("/activities/{activityId}")
    public ResponseEntity<DeleteActivityResponse> deleteActivity(
            @PathVariable("activityId") Long activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.status(HttpStatus.OK).body(new DeleteActivityResponse("success"));
    }
}
