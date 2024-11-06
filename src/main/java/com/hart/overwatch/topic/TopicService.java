package com.hart.overwatch.topic;

import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hart.overwatch.topic.dto.TopicDto;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.topic.request.UpdateTopicRequest;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.tag.dto.TagDto;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    private final UserService userService;

    private final PaginationService paginationService;

    @Autowired
    public TopicService(TopicRepository topicRepository, UserService userService,
            PaginationService paginationService) {
        this.topicRepository = topicRepository;
        this.userService = userService;
        this.paginationService = paginationService;
    }

    public Topic getTopicById(Long topicId) {
        return topicRepository.findById(topicId).orElseThrow(() -> new NotFoundException(
                String.format("Cannot find a topic with the id %d", topicId)));
    }

    private boolean topicExists(String title) {
        Topic existingTopic = topicRepository.findByTitle(title);

        return existingTopic != null;

    }

    public Topic updateTopic(UpdateTopicRequest request, Long topicId) {
        String description = Jsoup.clean(request.getDescription(), Safelist.none());
        Topic topic = getTopicById(topicId);

        topic.setDescription(description);
        topicRepository.save(topic);

        return topic;
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
        topicDto.setTotalCommentCount(topic.getComments().size());

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

    public PaginationDto<TopicDto> getTopics(int page, int pageSize, String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<Topic> result = this.topicRepository.findAll(pageable);

        List<TopicDto> topicDtos =
                result.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

        return new PaginationDto<TopicDto>(topicDtos, result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
    }


    public PaginationDto<TopicDto> getTopicsWithTags(int page, int pageSize, String direction,
            String query) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<Topic> result = this.topicRepository.findTopicWithTags(pageable, query);

        List<TopicDto> topicDtos =
                result.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

        return new PaginationDto<TopicDto>(topicDtos, result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
    }

    public PaginationDto<TopicDto> getAllUserTopics(Long userId, int page, int pageSize,
            String direction) {

        Pageable pageable = this.paginationService.getPageable(page, pageSize, direction);

        Page<Topic> result = this.topicRepository.findAllByUserId(pageable, userId);

        List<TopicDto> topicDtos =
                result.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

        return new PaginationDto<TopicDto>(topicDtos, result.getNumber(), pageSize,
                result.getTotalPages(), direction, result.getTotalElements());
    }


}
