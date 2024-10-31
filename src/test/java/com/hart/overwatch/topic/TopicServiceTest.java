package com.hart.overwatch.topic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
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

}

