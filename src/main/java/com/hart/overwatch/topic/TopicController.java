package com.hart.overwatch.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.topic.response.CreateTopicResponse;
import com.hart.overwatch.topic.response.GetTopicsResponse;
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

    @GetMapping("/search")
    public ResponseEntity<GetTopicsResponse> searchTopics(@RequestParam("query") String query) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetTopicsResponse("success", topicService.searchTopics(query)));
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<GetTopicResponse> getTopic(@PathVariable("topicId") Long topicId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new GetTopicResponse("success", topicService.getTopic(topicId)));
    }

    @PostMapping("")
    public ResponseEntity<CreateTopicResponse> createTopic(
            @Valid @RequestBody CreateTopicRequest request) {
        topicManagementService.handleCreateTopic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTopicResponse("success"));
    }

}
