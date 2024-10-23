package com.hart.overwatch.topic;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    private final UserService userService;

    @Autowired
    public TopicService(TopicRepository topicRepository, UserService userService) {
        this.topicRepository = topicRepository;
        this.userService = userService;
    }

    public Topic getTopicById(Long topicId) {
        return topicRepository.findById(topicId).orElseThrow(() -> new NotFoundException(
                String.format("Cannot find a topic with the id %d", topicId)));
    }

    private boolean topicExists(String title) {
        Topic existingTopic = topicRepository.findByTitle(title);

        return existingTopic != null;

    }

    public Topic createTopic(CreateTopicRequest request) {
        String title = Jsoup.clean(request.getTitle().toLowerCase(), Safelist.none());
        String description = Jsoup.clean(request.getDescription(), Safelist.none());

        if (topicExists(title)) {
            throw new BadRequestException(
                    String.format("A topic with the title %s already exists", title));
        }

        User user = userService.getUserById(request.getUserId());

        Topic topic = new Topic(title, description, user);

        topicRepository.save(topic);

        return topic;
    }
}
