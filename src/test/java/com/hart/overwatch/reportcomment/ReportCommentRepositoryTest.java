package com.hart.overwatch.reportcomment;

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
@Sql(scripts = "classpath:reset_report_comment_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ReportCommentRepositoryTest {

    @Autowired
    private ReportCommentRepository reportCommentRepository;

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

    private ReportComment reportComment;

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

    private ReportComment createReportComment(User user, Comment comment) {
        ReportComment reportCommentEntity = new ReportComment();
        reportCommentEntity.setDetails("details");
        reportCommentEntity.setReason(ReportReason.MISINFORMATION);
        reportCommentEntity.setReportedBy(user);
        reportCommentEntity.setComment(comment);
        reportCommentEntity.setCreatedAt(LocalDateTime.now());

        reportCommentRepository.save(reportCommentEntity);
        return reportCommentEntity;
    }

    @BeforeEach
    public void setUp() {
        user = createUser("john@gmail.com");
        topic = createTopic(user);
        tags = createTags();
        comment = createComment(user, topic);
        reportComment = createReportComment(user, comment);

        topic.setTags(tags);
        tags.forEach(tag -> tag.setTopics(List.of(topic)));
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        topic.getTags().clear();
        topicRepository.save(topic);
        reportCommentRepository.deleteAll();
        commentRepository.deleteAll();
        tagRepository.deleteAll();
        topicRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }
}


