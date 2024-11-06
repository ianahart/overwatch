package com.hart.overwatch.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.topic.request.UpdateTopicRequest;
import com.hart.overwatch.topic.response.CreateTopicResponse;
import com.hart.overwatch.topic.response.GetAllTopicsResponse;
import com.hart.overwatch.topic.response.GetTopicsResponse;
import com.hart.overwatch.topic.response.UpdateTopicResponse;
import com.hart.overwatch.topic.response.GetTopicResponse;
import com.hart.overwatch.topicmanagement.TopicManagementService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/topics")
public class TopicController {

    private final TopicService topicService;

    private final TopicManagementService topicManagementService;

    @Autowired
    public TopicController(TopicService topicService,
            TopicManagementService topicManagementService) {
        this.topicService = topicService;
        this.topicManagementService = topicManagementService;
    }

    @GetMapping(path = "/search")
    public ResponseEntity<GetTopicsResponse> searchTopics(@RequestParam("query") String query) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetTopicsResponse("success", topicService.searchTopics(query)));
    }

    @GetMapping(path = "/{topicId}")
    public ResponseEntity<GetTopicResponse> getTopic(@PathVariable("topicId") Long topicId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetTopicResponse("success", topicService.getTopic(topicId)));
    }

    @PostMapping(path = "")
    public ResponseEntity<CreateTopicResponse> createTopic(
            @Valid @RequestBody CreateTopicRequest request) {
        topicManagementService.handleCreateTopic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTopicResponse("success"));
    }

    @GetMapping(path = "")
    public ResponseEntity<GetAllTopicsResponse> getAllTopics(@RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetAllTopicsResponse("success",
                topicService.getTopics(page, pageSize, direction)));
    }


    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<GetAllTopicsResponse> getAllUserTopics(
            @PathVariable("userId") Long userId, @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetAllTopicsResponse("success",
                topicService.getAllUserTopics(userId, page, pageSize, direction)));
    }

    @GetMapping(path = "/tags")
    public ResponseEntity<GetAllTopicsResponse> getTopicsWithTags(@RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize, @RequestParam("direction") String direction,
            @RequestParam("query") String query) {

        return ResponseEntity.status(HttpStatus.OK).body(new GetAllTopicsResponse("success",
                topicService.getTopicsWithTags(page, pageSize, direction, query)));
    }

    @PatchMapping(path = "/{topicId}")
    public ResponseEntity<UpdateTopicResponse> updateTopic(@Valid @RequestBody UpdateTopicRequest request, @PathVariable("topicId") Long topicId) {
        topicManagementService.handleUpdateTopic(request, topicId);
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateTopicResponse("success"));
    }

}
