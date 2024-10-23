package com.hart.overwatch.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.topic.response.CreateTopicResponse;
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

    @PostMapping("")
    public ResponseEntity<CreateTopicResponse> createTopic(
            @Valid @RequestBody CreateTopicRequest request) {
        topicManagementService.handleCreateTopic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateTopicResponse("success"));
    }

}
