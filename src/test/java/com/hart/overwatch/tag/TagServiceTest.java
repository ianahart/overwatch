package com.hart.overwatch.tag;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    TopicRepository topicRepository;

    @Mock
    TagRepository tagRepository;

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
        user = createUser();
        topic = createTopic(user);
        tags = createTags();

        topic.setTags(tags);
        tags.forEach(tag -> tag.setTopics(List.of(topic)));
    }

    @Test
    public void TagService_CreateTags_With_New_Tags_ReturnNothing() {
        List<String> newTags = List.of("javascript", "html");

        Topic emptyTopic = new Topic();
        emptyTopic.setId(2L);
        emptyTopic.setTags(new ArrayList<>());

        for (String newTag : newTags) {
            String cleanedTag = Jsoup.clean(newTag, Safelist.none());
            when(tagRepository.findByName(cleanedTag)).thenReturn(Optional.ofNullable(null));
        }

        tagService.createTags(newTags, emptyTopic);

        verify(tagRepository, times(2)).save(any(Tag.class));
        Assertions.assertThat(emptyTopic.getTags().size()).isEqualTo(2);
        Assertions.assertThat(emptyTopic.getTags().get(0).getName()).isEqualTo("javascript");
        Assertions.assertThat(emptyTopic.getTags().get(1).getName()).isEqualTo("html");
    }

}


