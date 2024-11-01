package com.hart.overwatch.comment;

import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.comment.dto.CommentDto;
import com.hart.overwatch.commentvote.CommentVoteRepository;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.profile.ProfileRepository;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.tag.Tag;
import com.hart.overwatch.tag.TagRepository;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_comment_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository ProfileRepository;

    @Autowired
    CommentVoteRepository commentVoteRepository;


    @PersistenceContext
    private EntityManager entityManager;

    private Topic topic;

    private User user;

    private List<Tag> tags = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();


    private User createUser() {
        Boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("https://imgur.com/profile-pic");
        ProfileRepository.save(profile);

        User userEntity = new User("john@mail.com", "John", "Doe", "John Doe", Role.USER, loggedIn,
                profile, "Test12345%", new Setting());
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
        tagEntities.add(new Tag("spring boot"));
        tagEntities.add(new Tag("java"));
        tagRepository.saveAll(tagEntities);
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
        commentRepository.saveAll(comments);
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

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        topic.getTags().clear();
        topicRepository.save(topic);
        commentRepository.deleteAll();
        tagRepository.deleteAll();
        topicRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    public void CommentRepository_GetCommentsByTopicIdWithVoteDifference_ReturnPageOfCommentDto() {
        Long topicId = topic.getId();
        Pageable pageable = PageRequest.of(0, 10);

        Page<CommentDto> result =
                commentRepository.getCommentsByTopicIdWithVoteDifference(topicId, pageable);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(comments.size());
    }

    @Test
    public void CommentRepository_GetCommentsByTopicId_ReturnPageOfCommentDto() {
        Long topicId = topic.getId();
        Pageable pageable = PageRequest.of(0, 10);

        Page<CommentDto> result = commentRepository.getCommentsByTopicId(topicId, pageable);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(comments.size());
    }

}


