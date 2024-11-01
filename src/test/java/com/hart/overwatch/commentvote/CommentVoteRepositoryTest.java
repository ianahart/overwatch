package com.hart.overwatch.commentvote;

import java.time.LocalDateTime;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentRepository;
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
public class CommentVoteRepositoryTest {

    @Autowired
    private CommentVoteRepository commentVoteRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository ProfileRepository;

    @Autowired
    CommentRepository commentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Topic topic;

    private User user;

    private List<Tag> tags = new ArrayList<>();

    private CommentVote commentVote;

    private Comment comment;


    private User createUser(String email) {
        Boolean loggedIn = true;
        Profile profile = new Profile();
        profile.setAvatarUrl("https://imgur.com/profile-pic");
        ProfileRepository.save(profile);

        User userEntity = new User(email, "John", "Doe", "John Doe", Role.USER, loggedIn, profile,
                "Test12345%", new Setting());
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



    private Comment createComment(User user, Topic topic) {
        Comment commentEntity = new Comment();
        commentEntity.setTopic(topic);
        commentEntity.setUser(user);
        commentEntity.setContent(String.format("content"));
        commentEntity.setIsEdited(false);
        commentRepository.save(commentEntity);

        return commentEntity;
    }

    private CommentVote createCommentVote(User user, Comment comment) {
        CommentVote commentVoteEntity = new CommentVote();
        commentVoteEntity.setVoteType("UPVOTE");
        commentVoteEntity.setCreatedAt(LocalDateTime.now());
        commentVoteEntity.setUser(user);
        commentVoteEntity.setComment(comment);

        commentVoteRepository.save(commentVoteEntity);
        return commentVoteEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser("john@gmail.com");
        topic = createTopic(user);
        tags = createTags();
        comment = createComment(user, topic);
        commentVote = createCommentVote(user, comment);

        topic.setTags(tags);
        tags.forEach(tag -> tag.setTopics(List.of(topic)));
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        topic.getTags().clear();
        topicRepository.save(topic);
        commentVoteRepository.deleteAll();
        commentRepository.deleteAll();
        tagRepository.deleteAll();
        topicRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void CommentVoteRepository_FindByCommentIdAndUserId() {
        boolean exists =
                commentVoteRepository.findByCommentIdAndUserId(comment.getId(), user.getId());
        Assertions.assertThat(exists).isTrue();
    }

}


