package com.hart.overwatch.topic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.ForbiddenException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.tag.dto.TagDto;
import com.hart.overwatch.topic.dto.TopicDto;
import com.hart.overwatch.topic.request.CreateTopicRequest;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TopicServiceTest {

    @InjectMocks
    private TopicService topicService;

    @Mock
    TopicRepository topicRepository;

    @Mock
    private UserService userService;

    @Mock
    DatabaseSetupService databaseSetupService;

    @Mock
    private PaginationService paginationService;
    private Topic topic;

    private User user;

    private List<Tag> tags = new ArrayList<>();


    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userEntity.setId(1L);
        return userEntity;
    }

    private Topic createTopic(User user) {
        Topic topicEntity = new Topic("title", "description", user);
        topicEntity.setId(1L);
        return topicEntity;
    }

    private List<Tag> createTags() {
        List<Tag> tagEntities = new ArrayList<>();
        tagEntities.add(new Tag(1L, "spring boot"));
        tagEntities.add(new Tag(2L, "java"));
        return tagEntities;
    }

    @BeforeEach
    public void setUp() {
        databaseSetupService.createTsvectorTrigger();
        user = createUser();
        topic = createTopic(user);
        tags = createTags();

        topic.setTags(tags);
        tags.forEach(tag -> tag.setTopics(List.of(topic)));
    }

    @Test
    public void TopicService_GetTopicById_ThrowNotFoundException() {
        Long nonExistentTopicId = 999L;
        when(topicRepository.findById(nonExistentTopicId)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThatThrownBy(() -> {
            topicService.getTopicById(nonExistentTopicId);
        }).isInstanceOf(NotFoundException.class).hasMessage(
                String.format("Cannot find a topic with the id %d", nonExistentTopicId));
    }

    @Test
    public void TopicService_GetTopicById_ReturnTopic() {
        Long topicId = topic.getId();

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));

        Topic returnedTopic = topicService.getTopicById(topicId);

        Assertions.assertThat(returnedTopic).isNotNull();
        Assertions.assertThat(returnedTopic.getId()).isEqualTo(topic.getId());
    }

    @Test
    public void TopicService_CreateTopic_ThrowBadRequestExceptionAlreadyExists() {
        CreateTopicRequest request =
                new CreateTopicRequest("title", "description", List.of("spring boot", "java"));

        when(topicRepository.findByTitle(request.getTitle())).thenReturn(topic);

        Assertions.assertThatThrownBy(() -> {
            topicService.createTopic(request);
        }).isInstanceOf(BadRequestException.class).hasMessage(
                String.format("A topic with the title %s already exists", request.getTitle()));
    }

    @Test
    public void TopicService_CreateTopic_ReturnTopic() {
        CreateTopicRequest request = new CreateTopicRequest("new title", "new description",
                List.of("java", "spring boot"));
        request.setUserId(user.getId());

        when(topicRepository.findByTitle(request.getTitle())).thenReturn(null);

        when(userService.getUserById(request.getUserId())).thenReturn(user);

        Topic newTopic = new Topic(request.getTitle(), request.getDescription(), user);
        topic.setId(2L);
        topic.setTags(List.of(new Tag(3L, request.getTags().get(0)),
                new Tag(4L, request.getTags().get(1))));

        when(topicRepository.save(any(Topic.class))).thenReturn(newTopic);

        Topic returnedTopic = topicService.createTopic(request);

        Assertions.assertThat(returnedTopic).isNotNull();
        Assertions.assertThat(returnedTopic.getId()).isEqualTo(newTopic.getId());
        Assertions.assertThat(returnedTopic.getTitle()).isEqualTo(newTopic.getTitle());
        Assertions.assertThat(returnedTopic.getDescription()).isEqualTo(newTopic.getDescription());
        Assertions.assertThat(returnedTopic.getTags().size()).isEqualTo(newTopic.getTags().size());
    }

    @Test
    public void TopicService_SearchTopics_ThrowBadRequestException() {
        String query = null;

        Assertions.assertThatThrownBy(() -> {
            topicService.searchTopics(query);
        }).isInstanceOf(BadRequestException.class).hasMessage("Please provide a search term");
    }

    @Test
    public void TopicService_SearchTopics_ReturnListOfTopicDto() {
        String query = "title";
        String fuzzyQuery = query + ":*";

        when(topicRepository.searchTopics(fuzzyQuery)).thenReturn(List.of(topic));

        List<TopicDto> topicDtos = topicService.searchTopics(query);

        Assertions.assertThat(topicDtos).hasSize(1);
    }

    @Test
    public void TopicService_GetTopic_ThrowBadRequestException() {
        Long topicId = null;

        Assertions.assertThatThrownBy(() -> {
            topicService.getTopic(topicId);
        }).isInstanceOf(BadRequestException.class).hasMessage("Missing topicId. Please try again");
    }

    @Test
    public void TopicService_GetTopic_ReturnTopicDto() {
        Long topicId = topic.getId();

        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));

        TopicDto topicDto = topicService.getTopic(topicId);

        Assertions.assertThat(topicDto.getId()).isEqualTo(topic.getId());
        Assertions.assertThat(topicDto.getTitle()).isEqualTo(topic.getTitle());
        Assertions.assertThat(topicDto.getDescription()).isEqualTo(topic.getDescription());
        Assertions.assertThat(topicDto.getTags()).extracting(TagDto::getName).containsAll(
                topic.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
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

    @Test
    public void TopicService_GetTopics_ReturnPaginationDtoOfTopicDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        Topic topic = new Topic();
        topic.setTitle("title");

        Page<Topic> pageResult = new PageImpl<>(Collections.singletonList(topic), pageable, 1);

        List<TopicDto> topicDtos = pageResult.getContent().stream().map(this::convertToDto)
                .collect(Collectors.toList());
        PaginationDto<TopicDto> expectedPaginationDto =
                new PaginationDto<>(topicDtos, pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(topicRepository.findAll(pageable)).thenReturn(pageResult);

        PaginationDto<TopicDto> actualPaginationDto =
                topicService.getTopics(page, pageSize, direction);

        Assertions.assertThat(actualPaginationDto).usingRecursiveComparison()
                .isEqualTo(expectedPaginationDto);
    }

    @Test
    public void GetTopicsWithTags_ReturnPaginationDtoOfTopicDto() {
        int page = 0;
        int pageSize = 3;
        String direction = "next";

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        Topic topic = new Topic();
        topic.setTitle("title");

        Page<Topic> pageResult = new PageImpl<>(Collections.singletonList(topic), pageable, 1);

        List<TopicDto> topicDtos = pageResult.getContent().stream().map(this::convertToDto)
                .collect(Collectors.toList());
        PaginationDto<TopicDto> expectedPaginationDto =
                new PaginationDto<>(topicDtos, pageResult.getNumber(), pageSize,
                        pageResult.getTotalPages(), direction, pageResult.getTotalElements());

        when(paginationService.getPageable(page, pageSize, direction)).thenReturn(pageable);
        when(topicRepository.findTopicWithTags(pageable, "title")).thenReturn(pageResult);

        PaginationDto<TopicDto> actualPaginationDto =
                topicService.getTopicsWithTags(page, pageSize, direction, "title");

        Assertions.assertThat(actualPaginationDto).usingRecursiveComparison()
                .isEqualTo(expectedPaginationDto);

    }
}

