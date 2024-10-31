package com.hart.overwatch.comment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
import com.hart.overwatch.advice.BadRequestException;
import com.hart.overwatch.advice.NotFoundException;
import com.hart.overwatch.pagination.PaginationService;
import com.hart.overwatch.pagination.dto.PaginationDto;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserService;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    TopicRepository topicRepository;

    @Mock
    private UserService userService;


    @Mock
    private PaginationService paginationService;


    private Topic topic;

    private User user;

    private List<Tag> tags = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();


    private User createUser() {
        Boolean loggedIn = true;
        Profile profileEntity = new Profile();
        profileEntity.setAvatarUrl("https://imgur.com/profile-pic");
        profileEntity.setId(1L);

        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profileEntity, "Test12345%", new Setting());
        userEntity.setId(1L);;
        return userEntity;
    }

    private Topic createTopic(User user) {
        Topic topicEntity = new Topic("title", "description", user);
        topicRepository.save(topicEntity);
        return topicEntity;
    }

    private List<Tag> createTags() {
        List<Tag> tagEntities = new ArrayList<>();
        tagEntities.add(new Tag(1L, "spring boot"));
        tagEntities.add(new Tag(2L, "java"));
        return tagEntities;
    }

    private List<Comment> createComments(User user, Topic topic) {
        int numOfComments = 3;
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < numOfComments; i++) {
            Comment comment = new Comment();
            comment.setId(Long.valueOf(i + 1));
            comment.setTopic(topic);
            comment.setUser(user);
            comment.setContent(String.format("content-%d", i + 1));
            comment.setIsEdited(false);
            comments.add(comment);
        }
        return comments;
    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        topic = createTopic(user);
        tags = createTags();
        comments = createComments(user, topic);

        topic.setTags(tags);
        tags.forEach(tag -> tag.setTopics(List.of(topic)));
    }

}


