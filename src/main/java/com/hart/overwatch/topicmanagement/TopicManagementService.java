package com.hart.overwatch.topicmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hart.overwatch.tag.TagService;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicService;
import com.hart.overwatch.topic.request.CreateTopicRequest;

@Service
public class TopicManagementService {

    private final TopicService topicService;

    private final TagService tagService;


    @Autowired
    public TopicManagementService(TopicService topicService, TagService tagService) {
        this.topicService = topicService;
        this.tagService = tagService;
    }


    @Transactional
    public void handleCreateTopic(CreateTopicRequest request) {
        Topic topic = topicService.createTopic(request);
        tagService.createTags(request.getTags(), topic);
    }
}
