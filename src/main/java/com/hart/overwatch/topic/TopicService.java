package com.hart.overwatch.topic;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hart.overwatch.topic.dto.TopicDto;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.tag.dto.TagDto;

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

    private TopicDto convertToDto(Topic topic) {
        TopicDto topicDto = new TopicDto();

        topicDto.setId(topic.getId());
        topicDto.setTitle(topic.getTitle());
        topicDto.setDescription(topic.getDescription());
        List<TagDto> tagDtos = topic.getTags().stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName())).toList();
        topicDto.setTags(tagDtos);

        return topicDto;
    }

    public List<TopicDto> searchTopics(String query) {
        if (query == null || query.length() == 0) {
            throw new BadRequestException("Please provide a search term");
        }

        String fuzzyQuery = query.toLowerCase() + ":*";
        List<Topic> topics = topicRepository.searchTopics(fuzzyQuery.toLowerCase());
        List<TopicDto> topicDtos =
                topics.stream().map(this::convertToDto).collect(Collectors.toList());

        return topicDtos;
    }

    public TopicDto getTopic(Long topicId) {
        if (topicId == null) {
            throw new BadRequestException("Missing topicId. Please try again");
        }
        Topic topic = getTopicById(topicId);

        return convertToDto(topic);
    }
}
