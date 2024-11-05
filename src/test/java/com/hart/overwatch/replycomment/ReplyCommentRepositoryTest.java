package com.hart.overwatch.replycomment;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.hart.overwatch.comment.Comment;
import com.hart.overwatch.comment.CommentRepository;
import com.hart.overwatch.config.DatabaseSetupService;
import com.hart.overwatch.profile.Profile;
import com.hart.overwatch.replycomment.dto.ReplyCommentDto;
import com.hart.overwatch.setting.Setting;
import com.hart.overwatch.topic.Topic;
import com.hart.overwatch.topic.TopicRepository;
import com.hart.overwatch.user.Role;
import com.hart.overwatch.user.User;
import com.hart.overwatch.user.UserRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(DatabaseSetupService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:reset_reply_comment_sequences.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class ReplyCommentRepositoryTest {

    @Autowired
    private ReplyCommentRepository replyCommentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TopicRepository topicRepository;


    @PersistenceContext
    private EntityManager entityManager;

    private User user;

    private Comment comment;

    private ReplyComment replyComment;



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

    private Comment createComment(User user, Topic topic) {
        Comment commentEntity = new Comment();
        commentEntity.setUser(user);
        commentEntity.setTopic(topic);
        commentEntity.setContent("content");
        commentEntity.setIsEdited(false);
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentEntity.setReactions(new ArrayList<>());
        commentEntity.setCommentVotes(new ArrayList<>());
        commentRepository.save(commentEntity);
        return commentEntity;
    }


    private ReplyComment createReplyComment(User user, Comment comment) {
        LocalDateTime createdAt = LocalDateTime.now();
        ReplyComment replyCommentEntity = new ReplyComment();
        replyCommentEntity.setIsEdited(false);
        replyCommentEntity.setContent("content");
        replyCommentEntity.setUser(user);
        replyCommentEntity.setComment(comment);
        replyCommentEntity.setCreatedAt(createdAt);
        replyCommentRepository.save(replyCommentEntity);

        return replyCommentEntity;

    }

    @BeforeEach
    public void setUp() {
        user = createUser();
        Topic topic = createTopic(user);
        comment = createComment(user, topic);
        replyComment = createReplyComment(user, comment);

    }

    @AfterEach
    public void tearDown() {
        System.out.println("Tearing down the test data...");
        replyCommentRepository.deleteAll();
        commentRepository.deleteAll();
        topicRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void ReplyCommentRepository_CountByUserIdAndCreatedAtAfter_ReturnInt() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        replyCommentRepository.save(new ReplyComment(false, "content", user, comment));
        int replyCommentCount =
                replyCommentRepository.countByUserIdAndCreatedAtAfter(user.getId(), fiveMinutesAgo);

        Assertions.assertThat(replyCommentCount).isEqualTo(2);
    }

    @Test
    public void ReplyCommentRepository_FindReplyCommentsByCommentId_ReturnPageOfReplyCommentDto() {
        Pageable pageable = PageRequest.of(0, 3);
        Long commentId = comment.getId();

        Page<ReplyCommentDto> result =
                replyCommentRepository.findReplyCommentsByCommentId(pageable, commentId);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        ReplyCommentDto replyCommentDto = result.getContent().get(0);
        Assertions.assertThat(replyCommentDto.getId()).isEqualTo(replyComment.getId());
        Assertions.assertThat(replyCommentDto.getUserId())
                .isEqualTo(replyComment.getUser().getId());
        Assertions.assertThat(replyCommentDto.getContent()).isEqualTo(replyComment.getContent());
        Assertions.assertThat(replyCommentDto.getFullName())
                .isEqualTo(replyComment.getUser().getFullName());
    }

    @Test
    public void ReplyCommentRepository_FindReplyCommentsByUserIdAndCommentId_ReturnPageOfReplyCommentDto() {
        int page = 0;
        int pageSize = 3;
        Long userId = user.getId();
        Long commentId = comment.getId();
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<ReplyCommentDto> result = replyCommentRepository
                .findReplyCommentsByUserIdAndCommentId(pageable, userId, commentId);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        ReplyCommentDto replyCommentDto = result.getContent().get(0);
        Assertions.assertThat(replyCommentDto.getId()).isEqualTo(replyComment.getId());
        Assertions.assertThat(replyCommentDto.getUserId())
                .isEqualTo(replyComment.getUser().getId());
        Assertions.assertThat(replyCommentDto.getContent()).isEqualTo(replyComment.getContent());
        Assertions.assertThat(replyCommentDto.getFullName())
                .isEqualTo(replyComment.getUser().getFullName());

    }
}
