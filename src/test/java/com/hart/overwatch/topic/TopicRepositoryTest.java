package com.hart.overwatch.topic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.mockito.Mockito.when;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.tag.TagRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_topic_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DatabaseSetupService databaseSetupService;

    @PersistenceContext
    private EntityManager entityManager;

    private Topic topic;

    private User user;

    private List<Tag> tags = new ArrayList<>();


    private User createUser() {
        Boolean loggedIn = true;
        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                new Profile(), "Test12345%", new Setting());
        userRepository.save(userEntity);
        return userEntity;
    }

    private Topic createTopic(User user) {
        Topic topicEntity = new Topic("title", "description", user);
        topicRepository.save(topicEntity);
        return topicEntity;
    }

    private List<Tag> createTags() {
        List<Tag> tagEntities = new ArrayList<>();
        tagEntities.add(new Tag("Spring boot"));
        tagEntities.add(new Tag("Java"));
        tagRepository.saveAll(tagEntities);
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

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        topic.getTags().clear();
        topicRepository.save(topic);
        tagRepository.deleteAll();
        topicRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void TopicRepository_FindByTitle_ReturnTopic() {
        Topic returnedTopic = topicRepository.findByTitle(topic.getTitle());

        Assertions.assertThat(returnedTopic).isNotNull();
        Assertions.assertThat(returnedTopic.getTitle()).isEqualTo(topic.getTitle());
    }

    @Test
    public void TopicRepository_SearchTopics_ReturnListOfTopic() {
        String query = "title:*";
        List<Topic> returnedTopics = topicRepository.searchTopics(query);

        Assertions.assertThat(returnedTopics).isNotEmpty();
        Assertions.assertThat(returnedTopics).hasSize(1);
        Assertions.assertThat(returnedTopics.get(0).getTitle()).isEqualTo(topic.getTitle());
    }


}


